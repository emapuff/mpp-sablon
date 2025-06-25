// src/App.js
import React, { useEffect, useState } from "react";
import Login from "./LogIn";

import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

function App() {
    const [nickname, setNickname] = useState("");
    const [loggedIn, setLoggedIn] = useState(false);

    const [matrix] = useState(
        // 5×5 matrix of indices 0…24
        Array.from({ length: 5 }, (_, i) =>
            Array.from({ length: 5 }, (_, j) => i * 5 + j)
        )
    );
    const [selectedPosition, setSelectedPosition] = useState("");
    const [finished, setFinished] = useState(false);

    const [classament, setClassament] = useState([]);       // GET /game     :contentReference[oaicite:0]{index=0}
    const [personalGames, setPersonalGames] = useState([]); // GET /game?nickname=… :contentReference[oaicite:1]{index=1}
    const [configId, setConfigId] = useState(null);

    // load general leaderboard
    const loadClassament = async () => {
        const res = await fetch("http://localhost:8080/game");
        if (res.ok) {
            setClassament(await res.json());
        }
    };

    const loadPersonal = async () => {
        if (!nickname) {
            console.warn("Nu am nickname, nu trimit GET /game?nickname");
            return;
        }
        const res = await fetch(
            `http://localhost:8080/game?nickname=${encodeURIComponent(nickname)}`
        );
        if (res.ok) {
            const games = await res.json();
            setPersonalGames(games);

                           if (games.length > 0) {
                             setFinished(games[0].finished);
                          }

            if (games[0]?.configuration?.id != null && configId == null) {
                setConfigId(games[0].configuration.id);
            }
        }
    };

    // on mount → check login
    useEffect(() => {
        const isLogged = localStorage.getItem("loggedIn") === "true";
        const nick = localStorage.getItem("nickname");
        if (isLogged && nick) {
            setNickname(nick);
            setLoggedIn(true);
        }
    }, []);

    // after login → initial loads + websocket subscriptions
    useEffect(() => {
        if (!loggedIn) return;

        // first data fetch
        loadClassament();
        loadPersonal();

        // setup SockJS+STOMP
        const sock = new SockJS("http://localhost:8080/ws");           // :contentReference[oaicite:2]{index=2}
        const client = new Client({
            webSocketFactory: () => sock,
            onConnect: () => {
                // subscribe to general leaderboard updates
                client.subscribe("/topic/classament", (msg) => {
                    setClassament(JSON.parse(msg.body));
                });
                // subscribe to this player's game updates
                client.subscribe(`/topic/games/${nickname}`, (msg) => {
                    const payload = JSON.parse(msg.body);
                    if (payload.id != null && payload.finalScore != null) {
                        setPersonalGames((old) => {
                            const idx = old.findIndex((g) => g.id === payload.id);
                            if (idx >= 0) {
                                const copy = [...old];
                                copy[idx] = payload;
                                return copy;
                            } else {
                                return [payload, ...old];
                            }
                        });

                                if (payload.finished) {
                                    setFinished(true);
                                  }
                    }
                });
            },
        });
        client.activate();

        return () => client.deactivate();
    }, [loggedIn, nickname]);

    // send an attempt
    const handleAttempt = async () => {
        if (selectedPosition === "" || configId == null) return;
        const url = new URL("http://localhost:8080/game/attempt");
        url.searchParams.set("nickname", nickname);
        url.searchParams.set("position", selectedPosition);
        url.searchParams.set("index", configId);

        const res = await fetch(url, { method: "POST" });
        if (res.ok) {
            const dto = await res.json();
            console.log("Attempt result DTO:", dto);
            if (dto.gameFinished) {
                setFinished(true);
            }
            await loadPersonal();
        }
    };

    const handleLogout = () => {
        localStorage.removeItem("nickname");
        localStorage.removeItem("loggedIn");
        localStorage.removeItem("index");
        setNickname("");
        setLoggedIn(false);
        setSelectedPosition("");
        setFinished(false);
    };

    if (!loggedIn) {
        return (
            <Login
                onLogin={(nick) => {
                    setFinished(false);
                    setSelectedPosition("");
                    setPersonalGames([]);
                    const idx = Number(localStorage.getItem("index"));
                    localStorage.setItem("nickname", nick);
                    localStorage.setItem("loggedIn", "true");
                    setNickname(nick);
                    setConfigId(idx);
                    setLoggedIn(true);
                }}
            />
        );
    }

    return (
        <div className="app">
            <h1>Joc de Capcane</h1>

            <div className="matrix">
                {matrix.map((row, i) => (
                    <div key={i} style={{display: "flex"}}>
                        {row.map((cell) => (
                            <button
                                key={cell}
                                style={{
                                    width: 40,
                                    height: 40,
                                    margin: 2,
                                    background:
                                        Number(selectedPosition) === cell ? "#ddd" : "#fff",
                                }}
                                onClick={() => setSelectedPosition(String(cell))}
                            >
                                {cell}
                            </button>
                        ))}
                    </div>
                ))}
            </div>

            {/* Input & Send */}
            <div style={{margin: "1em 0"}}>
                <input
                    type="number"
                    placeholder="Introdu poziția"
                    value={selectedPosition}
                    onChange={(e) => setSelectedPosition(e.target.value)}
                    disabled={finished}
                />
                <button onClick={handleAttempt} disabled={finished}>
                    Trimite încercare
                </button>
            </div>

            {/* General leaderboard */}
            <h2>Clasament General</h2>
            <table border="1" cellPadding="4">
                <thead>
                <tr>
                    <th>Jucător</th>
                    <th>Scor final</th>
                </tr>
                </thead>
                <tbody>
                {classament.map((g) => (
                    <tr key={g.id}>
                        <td>{g.player.nickname}</td>
                        <td>{g.finalScore}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            {/* Personal leaderboard */}
            <h2>Clasament Personal</h2>
            <table border="1" cellPadding="4">
                <thead>
                <tr>
                    <th>Game ID</th>
                    <th>Scor</th>
                    <th>Terminată</th>
                </tr>
                </thead>
                <tbody>
                {personalGames.map((g) => (
                    <tr key={g.id}>
                        <td>{g.id}</td>
                        <td>{g.finalScore}</td>
                        <td>{g.finished ? "✅" : "❌"}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            <button onClick={handleLogout}>Logout</button>
        </div>
    );
}

export default App;
