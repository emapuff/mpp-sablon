import React, { useEffect, useState } from "react";
import Login from "./LogIn";
import './App.css';

function App() {
    const [checkedLogin, setCheckedLogin] = useState(false);
    const [loggedIn, setLoggedIn] = useState(false);
    const [classament, setClassament] = useState([]);
    const [configs, setConfigs] = useState([]);
    const [selectedPosition, setSelectedPosition] = useState("");
    const [matrix, setMatrix] = useState([
        ["C", "A", "T"],
        ["D", "O", "G"],
        ["P", "I", "G"]
    ]);
    const [word, setWord] = useState("");
    useEffect(() => {
        const saved = localStorage.getItem("loggedIn");
        if (saved === "true") {
            setLoggedIn(true);
        }
        setCheckedLogin(true);
    }, []);

    useEffect(() => {
        if (loggedIn) {
            loadClassament();
            // loadConfigs();
        }
    }, [loggedIn]);

    const loadClassament = async () => {
        const res = await fetch("http://localhost:8080/game");
        const data = await res.json();
        setClassament(data);
    };

    // const loadConfigs = async () => {
    //     const res = await fetch("http://localhost:8080/game");
    //     const data = await res.json();
    //     setConfigs(data);
    // };

    const handleAttempt = async () => {
        const nickname = localStorage.getItem("nickname");
        await fetch(`http://localhost:8080/game?nickname=${nickname}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(selectedPosition)
        });
    };

    const handleUpdateGame = async () => {
        const nickname = localStorage.getItem("nickname");
        await fetch(`http://localhost:8080/game?nickname=${nickname}`, {
            method: "PUT"
        });
    };

    if (!checkedLogin) return null;

    if (!loggedIn) {
        return <Login onLogin={() => setLoggedIn(true)} />;
    }

    return (
        <div className="game-container">
            <div><h1>Joc</h1></div>

            <div className="matrix">
                {matrix.map((row, i) => (
                    <div key={i} className="matrix-row">
                        {row.map((cell, j) => (
                            <button key={j} className="matrix-cell">{cell}</button>
                        ))}
                    </div>
                ))}
            </div>

            <div className="word-input">
                <input
                    value={word}
                    onChange={(e) => setWord(e.target.value)}
                    placeholder="Cuvântul format"
                />
                <input
                    value={selectedPosition}
                    onChange={(e) => setSelectedPosition(e.target.value)}
                    placeholder="Poziții (ex: 0,0-0,1-0,2)"
                />
                <button onClick={handleAttempt}>Trimite încercare</button>
                <button onClick={handleUpdateGame}>Finalizează joc</button>
            </div>

            <h2>Clasament</h2>
            <table>
                <thead>
                <tr>
                    <th>Jucător</th>
                    <th>Scor final</th>
                </tr>
                </thead>
                <tbody>
                {classament.map((g, i) => (
                    <tr key={i}>
                        <td>{g.player.nickname}</td>
                        <td>{g.finalScore}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            <h2>Configurări jocuri</h2>
            <table>
                <thead>
                <tr>
                    <th>Dimensiune</th>
                    <th>Limita timp</th>
                    <th>Acțiune</th>
                </tr>
                </thead>
                <tbody>
                {configs.map((conf, i) => (
                    <tr key={i}>
                        <td>{conf.size}</td>
                        <td>{conf.timeLimit}</td>
                        <td>
                            <button onClick={() => alert("Start joc cu config: " + conf.id)}>Start</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            <div >
                <button className="logout" onClick={() => {
                    localStorage.removeItem("loggedIn");
                    localStorage.removeItem("nickname");
                    setLoggedIn(false);
                }}>
                    Logout
                </button>
            </div>
        </div>

    )
        ;
}

export default App;
