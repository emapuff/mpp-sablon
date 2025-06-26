import React, { useState, useEffect } from 'react';

export default function AdminConfigs() {
    const [configs, setConfigs] = useState([]);
    const [editing, setEditing] = useState({}); // { [id]: draftValue }

    // 1) la încărcare, aduce toate configurațiile
    useEffect(() => {
        fetch('http://localhost:8080/admin', )
            .then(res => {
                if (!res.ok) throw new Error(`Fetch error: ${res.status}`);
                console.log(res.data);
                return res.json();
            })
            .then(data => setConfigs(data))
            .catch(err => console.error(err));
    }, []);

    // 2) când se scrie în input, salvăm în state
    const handleInputChange = (id, value) => {
        setEditing(prev => ({ ...prev, [id]: value }));
    };

    const handleUpdate = (id) => {
        const draft = editing[id];
        if (!draft) return;
        let payload;
        try {
            payload = JSON.parse(draft);       // transformă string-ul din input într-un obiect
        } catch {
            return alert("Formatează noua configurație ca JSON, ex: {\"A\":1, \"B\":2}");
        }

        fetch(`http://localhost:8080/admin/${id}`, {
            method: 'PUT',
            credentials: 'include',            // dacă foloseşti sesiuni/cookie-uri
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)      // trimite acum un obiect JSON
        })
            .then(res => {
                if (!res.ok) throw new Error(`Update failed: ${res.status}`);
                return res.json();
            })
            .then(updated => {
                setConfigs(prev => prev.map(c => c.id === updated.id ? updated : c));
                setEditing(prev => { const c = {...prev}; delete c[id]; return c; });
            })
            .catch(err => console.error(err));
    };

    return (
        <div>
            <h2>Administrare Configurații</h2>
            <table border="1" cellPadding="8" style={{ borderCollapse: 'collapse' }}>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Values</th>
                    <th>Nouă valoare</th>
                    <th>Acțiune</th>
                </tr>
                </thead>
                <tbody>
                {configs.map(conf => (
                    <tr key={conf.id}>
                        <td>{conf.id}</td>
                        <td>
                            {/* Presupunem că values e un obiect, îl afișăm ca JSON */}
                            <pre style={{ margin: 0 }}>
                  {JSON.stringify(conf.values, null, 2)}
                </pre>
                        </td>
                        <td>
                            <input
                                type="text"
                                value={editing[conf.id] ?? ''}
                                onChange={e => handleInputChange(conf.id, e.target.value)}
                                placeholder='ex: {"A":1,"B":2}'
                                style={{ width: '300px' }}
                            />
                        </td>
                        <td>
                            <button onClick={() => handleUpdate(conf.id)}>
                                Modifică
                            </button>
                        </td>
                    </tr>
                ))}
                {configs.length === 0 && (
                    <tr>
                        <td colSpan="4">Nu există configurații.</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
}
