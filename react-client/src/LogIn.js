import { useState } from "react";
import './Login.css';

export default function Login({ onLogin }) {
    const [nickname, setNickname] = useState("");
    const [showConfigs, setShowConfigs] = useState(false);
    const [configs, setConfigs] = useState([]);
    const [newConfig, setNewConfig] = useState("");
    const [editValues, setEditValues] = useState({});
    function getRandomInt(min, max) {
        min = Math.ceil(min);   // rotunjire în sus
        max = Math.floor(max);  // rotunjire în jos
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }
    const loadConfigs = async () => {
        const res = await fetch("http://localhost:8080/auth");
        if (res.ok) {
            const data = await res.json();
            setConfigs(data);
        }
    };
    const login = async () => {
        const res = await fetch("http://localhost:8080/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ nickname })
        });

        if (res.ok) {
            const numar = getRandomInt(1, 3);
            localStorage.setItem("index",numar);
            localStorage.setItem("nickname", nickname);
            localStorage.setItem("loggedIn", "true");
            onLogin(nickname);
        } else {
            alert("Autentificare eșuată");
        }
    };

    const handleCerinta5 = async () => {
        if (!showConfigs) {
            const res = await fetch("http://localhost:8080/auth");
            if (res.ok) {
                const data = await res.json();
                setConfigs(data);
                setShowConfigs(true);
            }
        } else {
            setShowConfigs(false);
        }
        if (!showConfigs) {
            await loadConfigs();
            setShowConfigs(true);
        }else {
                 setShowConfigs(false);
              }
    };

    const handleModify = async (id) => {
        const vals = editValues[id] || "";
        const res = await fetch(`http://localhost:8080/auth/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ values: vals })
        });
        if (res.ok) {
            const data = await (await fetch("http://localhost:8080/auth")).json();
            setConfigs(data);
            await loadConfigs();
        }
    };

    return (
        <div className="login-container">
            <h2>Autentificare</h2>
            <input
                placeholder="Poreclă"
                value={nickname}
                onChange={(e) => setNickname(e.target.value)}
            />
            <button onClick={login}>Login</button>
            <button onClick={handleCerinta5}>Cerinta 5</button>
            <table border="1" cellPadding="4" style={{marginTop: 10}}>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Values</th>
                    <th>Acțiuni</th>
                </tr>
                </thead>
                <tbody>
                {configs.map((c) => (
                    <tr key={c.id}>
                        <td>{c.id}</td>
                        <td>{c.values.join(",")}</td>
                        <td>
                            <input
                                placeholder="nou CSV"
                                value={editValues[c.id] || ""}
                                onChange={(e) =>
                                    setEditValues({
                                        ...editValues,
                                        [c.id]: e.target.value,
                                    })
                                }
                                style={{width: 120}}
                            />
                            <button onClick={() => handleModify(c.id)}>
                                Modifică
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
