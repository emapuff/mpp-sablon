
import { useState } from "react";
import './Login.css';
import {useNavigate} from "react-router-dom";

export default function Login({ onLogin }) {
    const navigate = useNavigate();
    const [nickname, setNickname] = useState("");
    const [configs, setConfigs] = useState([]);

    const login = async () => {
        const res = await fetch("http://localhost:8080/auth/login", {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ nickname })
        });

        if (res.ok) {
            localStorage.setItem("loggedIn", "true");
            onLogin();
        } else {
            alert("Autentificare eșuată");
        }
    };

    const handleCerinta5 = () => {
        navigate('/admin');
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
        </div>
    );
}
