import { BellOutlined, MenuOutlined } from "@ant-design/icons";
import { Button, Dropdown } from "antd";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import { useState } from "react";
import IzinOlustur from "./IzinOlustur";
import IzinTakvimi from "./IzinTakvimi";
import IzinOnay from "./IzinOnay";
import IzinTaleplerim from "./IzinTaleplerim";
import Dashboard from "./Dashboard";

const items = [
  {
    key: "1",
    label: <a href="/">Profil Ayarları</a>,
  },
  {
    key: "2",
    label: <a href="/">Tema Ayarları</a>,
  },
  {
    key: "3",
    label: <a href="/dil-secimi">Dil Seçimi</a>,
  },
  {
    key: "4",
    label: <a href="/">Çıkış</a>,
  },
];

function AnaEkran() {
  return (
    <div className="flex flex-col items-center justify-start h-full text-center bg-gradient-to-br from-gray-100 to-gray-300 rounded-lg shadow-inner p-5 pt-20">
      <h1 className="text-2xl sm:text-3xl font-bold text-gray-900">Hoş Geldiniz!</h1>
      <br />
      <p className="text-base sm:text-lg text-gray-800 max-w-xl">
        Sol menüyü kullanarak izin taleplerinizi oluşturabilir ve yönetebilirsiniz.
      </p>
    </div>
  );
}

function HomePage() {
  const [menuOpen, setMenuOpen] = useState(false);

  return (
    <div className="flex flex-col h-screen bg-gray-50 text-gray-800">
      <header className="fixed top-0 left-0 w-full flex items-center justify-between p-4 bg-gradient-to-r from-gray-200 to-gray-400 text-gray-900 shadow-md z-20">
        <div className="flex items-center space-x-4">
          <button
            className="sm:hidden text-2xl"
            onClick={() => setMenuOpen(!menuOpen)}
          >
            <MenuOutlined />
          </button>
          <div className="text-xl sm:text-2xl font-bold tracking-wide">
            İZİN YÖNETİMİ
          </div>
        </div>

        <div className="flex items-center space-x-4">
          <input
            className="w-32 sm:w-60 px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-400 focus:border-gray-400"
            type="text"
            placeholder="İzin Ara"
          />
          <BellOutlined className="text-xl hover:text-gray-600 transition cursor-pointer" />
          <Dropdown menu={{ items }} placement="bottomLeft">
            <Button>PROFİL</Button>
          </Dropdown>
        </div>
      </header>

      {/* Mobil Sidebar */}
      <aside
        className={`sm:hidden fixed top-16 left-0 w-64 h-full bg-gray-100 shadow-lg z-30 transform transition-transform duration-300 ease-in-out ${menuOpen ? "translate-x-0" : "-translate-x-full"
          }`}
      >
        <nav className="flex flex-col p-4 space-y-4">
          <SidebarLinks setMenuOpen={setMenuOpen} />
        </nav>
      </aside>

      <div className="flex flex-1 pt-[72px]">
        {/* Masaüstü Sidebar */}
        <aside className="hidden sm:block w-64 bg-gray-100 shadow-lg rounded-lg p-4 pt-12">
          <nav className="flex flex-col space-y-4">
            <SidebarLinks />
          </nav>
        </aside>

        <main className="flex-1 p-4 sm:p-12 overflow-y-auto">
          <Routes>
            <Route path="/" element={<AnaEkran />} />
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/izin-olustur" element={<IzinOlustur />} />
            <Route path="/izin-talepleri" element={<IzinTaleplerim />} />
            <Route path="/izin-onay" element={<IzinOnay />} />
            <Route path="/izin_takvimi" element={<IzinTakvimi />} />
          </Routes>
        </main>
      </div>
    </div>
  );
}

// Sidebar Linkleri ayrı component olarak
function SidebarLinks({ setMenuOpen }) {
  const handleClick = () => {
    if (setMenuOpen) setMenuOpen(false); // mobilde menüyü kapat
  };

  const linkClass =
    "px-4 py-2 text-sm font-medium text-gray-800 bg-gray-200 rounded-lg hover:bg-gray-300 hover:text-gray-900 transition duration-200";

  return (
    <>
      <Link to="/dashboard" className={linkClass} onClick={handleClick}>
        DASHBOARD
      </Link>
      <Link to="/izin-olustur" className={linkClass} onClick={handleClick}>
        İzin Talebi Oluştur
      </Link>
      <Link to="/izin-talepleri" className={linkClass} onClick={handleClick}>
        İzin Talepleri
      </Link>
      <Link to="/izin-onay" className={linkClass} onClick={handleClick}>
        Onay Bekleyenler
      </Link>
      <Link to="/izin_takvimi" className={linkClass} onClick={handleClick}>
        İzin Takvimi
      </Link>
      <Link to="/PersonelListesi" className={linkClass} onClick={handleClick}>
        Personel Listesi
      </Link>
      <Link to="/Raporlar" className={linkClass} onClick={handleClick}>
        Raporlar
      </Link>
    </>
  );
}

export default HomePage;
