import React, { useState } from 'react';
import { FaEye, FaEyeSlash, FaLock } from 'react-icons/fa6';

function ChangePasswordPage() {
    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');

    // Göster/gizle için state
    const [showCurrent, setShowCurrent] = useState(false);
    const [showNew, setShowNew] = useState(false);
    const [showConfirm, setShowConfirm] = useState(false);

    const handleSubmit = (e) => {
        e.preventDefault();

        if (newPassword !== confirmPassword) {
            setError("Yeni şifreler eşleşmiyor.");
            return;
        }

        if (newPassword.length < 8) {
            setError("Yeni şifre en az 8 karakter olmalıdır.");
            return;
        }

        setError("");
        console.log("Şifre başarıyla güncellendi.");
        // API çağrısı yapılabilir
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-teal-800 to-cyan-100">
            <div className="bg-white p-8 rounded-2xl shadow-2xl w-[350px] text-center flex flex-col justify-center">
                <div className="text-center mb-4">
                    <h1 className="text-2xl font-bold">ŞİFRE DEĞİŞTİR</h1>
                    <FaLock className="text-cyan-500 text-2xl mx-auto mt-2" />
                </div>

                {error && (
                    <div className="bg-red-100 text-red-600 px-4 py-2 rounded-md mb-4 text-sm">
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-4 text-left">
                    {/* Mevcut şifre */}
                    <div className="relative">
                        <input
                            type={showCurrent ? "text" : "password"}
                            placeholder="Mevcut şifreniz"
                            value={currentPassword}
                            onChange={(e) => setCurrentPassword(e.target.value)}
                            className="w-full pl-3 pr-10 py-3 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-cyan-500 focus:ring focus:ring-cyan-200"
                            required
                        />
                        <span
                            onClick={() => setShowCurrent(!showCurrent)}
                            className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 cursor-pointer"
                        >
                            {showCurrent ? <FaEye /> : <FaEyeSlash />}
                        </span>
                    </div>

                    {/* Yeni şifre */}
                    <div className="relative">
                        <input
                            type={showNew ? "text" : "password"}
                            placeholder="Yeni şifreniz"
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                            className="w-full pl-3 pr-10 py-3 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-cyan-500 focus:ring focus:ring-cyan-200"
                            required
                        />
                        <span
                            onClick={() => setShowNew(!showNew)}
                            className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 cursor-pointer"
                        >
                            {showNew ? <FaEye /> : <FaEyeSlash />}
                        </span>
                    </div>

                    {/* Yeni şifre tekrar */}
                    <div className="relative">
                        <input
                            type={showConfirm ? "text" : "password"}
                            placeholder="Yeni şifrenizi tekrar girin"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            className="w-full pl-3 pr-10 py-3 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-cyan-500 focus:ring focus:ring-cyan-200"
                            required
                        />
                        <span
                            onClick={() => setShowConfirm(!showConfirm)}
                            className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 cursor-pointer"
                        >
                            {showConfirm ? <FaEye /> : <FaEyeSlash />}
                        </span>
                    </div>

                    <button
                        type="submit"
                        className="bg-cyan-500 hover:bg-cyan-600 text-white py-3 w-full rounded-lg font-semibold transition"
                    >
                        Şifreyi Değiştir
                    </button>

                    <button
                        type="button"
                        onClick={() => window.history.back()}
                        className="mt-2 bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 w-full rounded-lg font-medium transition"
                    >
                        İptal Et
                    </button>
                </form>

                <div className="mt-6 bg-gray-100 text-gray-600 p-3 rounded-md text-sm text-left">
                    🔐 Güçlü bir şifre en az 8 karakterden oluşmalıdır.
                </div>
            </div>
        </div>
    );
}

export default ChangePasswordPage;
