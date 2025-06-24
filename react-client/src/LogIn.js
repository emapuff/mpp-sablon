import { useState } from "react";
import './Login.css';

export default function Login({ onLogin }) {
    const [nickname, setNickname] = useState("");

    const login = async () => {
        const res = await fetch("http://localhost:8080/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ nickname })
        });

        if (res.ok) {
            localStorage.setItem("loggedIn", "true");
            localStorage.setItem("nickname", nickname);
            onLogin();
        } else {
            alert("Autentificare eșuată");
        }
    };

    // const admin = async () => {
    //     const res = await fetch("http://localhost:8080/auth/admin", {
    //
    //     })
    // }

    return (
        <div className="login-container">
            <h2>Autentificare</h2>
            <input
                placeholder="Poreclă"
                value={nickname}
                onChange={(e) => setNickname(e.target.value)}
            />
            <button onClick={login}>Login</button>
            <button >Cerinta 5</button>
        </div>
    );
}
