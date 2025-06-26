import React, { useEffect, useState } from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./LogIn";
import AdminConfigs from "./AdminConfigs";
import SockJS                from 'sockjs-client';
import { Stomp }             from '@stomp/stompjs';

function GamePage({ onLogout }) {
    const [configLetters, setConfigLetters] = useState([]);    // cele 4 litere
    const [disabled, setDisabled] = useState([false, false, false, false]); // butoane disable
    const [attemptResult, setAttemptResult] = useState(null);  // { finalScore, letterChosen }
    const [classament, setClassament] = useState([]);          // lista ClassamentDTO
    const [wonGames, setWonGames] = useState([]);              // lista GameWon

    const nickname = localStorage.getItem("nickname");

    useEffect(() => {
        // 1) facem fetch iniţial
        fetch("http://localhost:8080/game/classament",{credentials: "include"})
            .then(res => {
                if (!res.ok) throw new Error(res.statusText);
                return res.json();
            })
            .then(data => {
                // dacă backend îți trimite { data: [...] }, folosește data.data
                const list = Array.isArray(data) ? data : data.data || [];
                setClassament(list);
            })
            .catch(err => {
                console.error("Error loading classament:", err);
                setClassament([]);   // ca să nu fie undefined
            });

        fetch("http://localhost:8080/game",{credentials: "include"})
          .then(res => res.json())
            .then(cfg => {
                  // cfg ar trebui să aibă proprietatea `keys` cu array-ul de litere
                      setConfigLetters(cfg.keys || []);
                })
            .catch(console.error);
        // 2) deschidem WebSocket + STOMP
        const socket = new SockJS("http://localhost:8080/ws");
        const client = Stomp.over(socket);

        client.connect({}, () => {
            // primele două subscribe-uri, dacă vrei şi la /topic/won
            client.subscribe("/topic/classament", msg => {
                setClassament(JSON.parse(msg.body));
            });
            client.subscribe("/topic/won", msg => {
                setWonGames(JSON.parse(msg.body));
            });
        });

        // 3) cleanup: deconectare când componenta se demontează
        return () => client.disconnect();

    }, []);

    const handleLetterClick = (index) => {
        fetch(`http://localhost:8080/game?nickname=${nickname}`, {
            method: "POST",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(index)
        })
            .then(res => res.json())
            .then(attempt => {
                setAttemptResult(attempt);
                setDisabled(prev => {
                    const next = [...prev];
                    next[index] = true;

                    // if (next.every(flag => flag)) {
                    //     refreshTables();
                    // }

                    return next;
                });
            })
            .catch(console.error);
    };


    return (
        <div className="game-container">
            <h1>Joc</h1>

            {/* 1. Lista de 4 butoane cu literele din config */}
            <div className="letter-list">
                {configLetters.map((ltr, idx) => (
                    <button
                        key={idx}
                        disabled={disabled[idx]}
                        onClick={() => handleLetterClick(idx)}
                        style={{ margin: "0 8px", padding: "12px", fontSize: "16px" }}
                    >
                        {ltr}
                    </button>
                ))}
            </div>

            {/* 2. Rezultatul ultimei încercări */}
            {attemptResult && (
                <div className="attempt-result" style={{ marginTop: "16px" }}>
                    <strong>Rezultat:</strong> Scor curent = {attemptResult.score},
                    Litera extrasă = {attemptResult.letter}
                </div>
            )}


            {/* 4. Tabel Clasament */}
            <h2 style={{ marginTop: "32px" }}>Clasament</h2>
            <table border="1" cellPadding="8" style={{ borderCollapse: "collapse" }}>
                <thead>
                <tr>
                    <th>Jucător</th>
                    <th>Scor final</th>
                    <th>Data start</th>
                </tr>
                </thead>
                <tbody>
                {classament.map((row, i) => (
                    <tr key={i}>
                        <td>{row.nume_player}</td>
                        <td>{row.scor}</td>
                        <td>{row.when}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            {/* 5. Tabel Jocuri câștigate */}
            <h2 style={{ marginTop: "32px" }}>Jocuri câștigate</h2>
            <table border="1" cellPadding="8" style={{ borderCollapse: "collapse" }}>
                <thead>
                <tr>
                    <th>Scor final</th>
                    <th>Litere rămase</th>
                </tr>
                </thead>
                <tbody>
                {wonGames.map((w, i) => (
                    <tr key={i}>
                        <td>{w.score}</td>
                        <td>{w.letters}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            {/* Logout */}
            <div style={{ marginTop: "32px" }}>
                <button
                    onClick={() => {
                        localStorage.removeItem("loggedIn");
                        localStorage.removeItem("nickname");
                        onLogout();
                    }}
                >
                    Logout
                </button>
            </div>
        </div>

    );
}

function App() {
    const [checkedLogin, setCheckedLogin] = useState(false);
    const [loggedIn, setLoggedIn]   = useState(false);

    useEffect(() => {
        const saved = localStorage.getItem("loggedIn");
        if (saved === "true") setLoggedIn(true);
        setCheckedLogin(true);
    }, []);

    const handleLogin = (nickname) => {
        localStorage.setItem("loggedIn", "true");
        localStorage.setItem("nickname", nickname);
        setLoggedIn(true);
    };

    if (!checkedLogin) return null; // aşteptăm să vedem dacă suntem logaţi

    return (
        <BrowserRouter>
            <Routes>

                <Route
                    path="/login"
                    element={
                        loggedIn
                            ? <Navigate to="/" replace />
                            : <Login onLogin={handleLogin} />
                    }
                />


                <Route
                    path="/"
                    element={
                        loggedIn
                            ? <GamePage onLogout={() => setLoggedIn(false)} />
                            : <Navigate to="/login" replace />
                    }
                />

                <Route path="/admin" element={<AdminConfigs />} />


                <Route path="*" element={<Navigate to="/login" replace />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
