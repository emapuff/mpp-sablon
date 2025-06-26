import React, { useEffect, useState } from "react";

function MemoryGame({ configLetters, onLetterClick, attemptResult, classament, wonGames, onLogout }) {
    const rows = 2;
    const cols = configLetters.length / rows;

    // Stări pentru matrice și butoane disabled
    const [matrix, setMatrix] = useState([]);
    const [disabledMatrix, setDisabledMatrix] = useState([]);

    // Construim matricea și starea de disabled la mount
    useEffect(() => {
        // Împărțim array-ul plat în rânduri
        const m = [];
        for (let r = 0; r < rows; r++) {
            m.push(configLetters.slice(r * cols, (r + 1) * cols));
        }
        setMatrix(m);
        setDisabledMatrix(Array(rows).fill(null).map(() => Array(cols).fill(false)));
    }, [configLetters]);

    const handleLetterClick = (r, c) => {
        const flatIndex = r * cols + c;
        onLetterClick(flatIndex, (secondIndex) => {
            // fallback, dacă back-end vrea poziții separate:
            const r2 = Math.floor(secondIndex / cols);
            const c2 = secondIndex % cols;

            setDisabledMatrix(prev => {
                const next = prev.map(row => [...row]);
                next[r][c] = true;
                next[r2][c2] = true;
                return next;
            });
        });

        // Dacă jocul e terminat, dezactivăm toate
        if (attemptResult?.finished) {
            setDisabledMatrix(prev => prev.map(row => row.map(() => true)));
        }
    };

    return (
        <div className="memory-game">
            <div className="grid">
                {matrix.map((row, r) => (
                    <div key={r} className="row">
                        {row.map((letter, c) => (
                            <button
                                key={`${r}-${c}`}
                                disabled={disabledMatrix[r][c]}
                                onClick={() => handleLetterClick(r, c)}
                                style={{ width: 60, height: 60, margin: 4, fontSize: 24 }}
                            >
                                {letter}
                            </button>
                        ))}
                    </div>
                ))}
            </div>

            {/* restul componentei: afișare attemptResult, clasament, wonGames, logout... */}
        </div>
    );
}

export default MemoryGame;
