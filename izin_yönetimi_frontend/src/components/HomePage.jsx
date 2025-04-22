import { BellOutlined } from "@ant-design/icons";
import { UserOutlined } from "@ant-design/icons";
import { Button, Dropdown, Space, Tooltip } from "antd";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import IzinOlustur from "./IzinOlustur";
import IzinTakvimi from "./IzinTakvimi";
import IzinOnay from "./IzinOnay";
import IzinTaleplerim from "./IzinTaleplerim";
import Dashboard from "./Dashboard";

const items = [
  {
    key: "1",
    label: (
      <a target="_blank" rel="noopener noreferrer" href="/">
        Profil Ayarları
      </a>
    ),
  },
  {
    key: "2",
    label: (
      <a target="_blank" rel="noopener noreferrer" href="/">
        Tema Ayarları
      </a>
    ),
  },

  {
    key: "3",
    label: (
      <a target="_blank" rel="noopener noreferrer" href="/dil-secimi">
        Dil Seçimi
      </a>
    ),
  },
  {
    key: "4",
    label: (
      <a target="_blank" rel="noopener noreferrer" href="/">
        Çıkış
      </a>
    ),
  },
];

function AnaEkran() {
  return (
    <div className="flex flex-col items-center justify-start h-full text-center bg-gradient-to-br from-gray-100 to-gray-300 rounded-lg shadow-inner p-5 pt-30">
      <h1 className="text-3xl font-bold text-gray-900">Hoş Geldiniz!</h1>
      <br />
      <p className="text-lg text-gray-800 max-w-xl">
        Sol menüyü kullanarak izin taleplerinizi oluşturabilir ve
        yönetebilirsiniz.
      </p>
    </div>
  );
}

function HomePage() {
  return (
    <div className="flex flex-col h-screen bg-gray-50 text-gray-800 ">
      <header className="fixed top-0 left-0 w-full flex items-center justify-between p-4 bg-gradient-to-r from-gray-200 to-gray-400 text-gray-900 shadow-md z-10 ">
        <div className="text-2xl font-bold tracking-wide ml-6">
          İZİN YÖNETİMİ
        </div>

        <div className="flex items-center space-x-4">
          <input
            className="px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-400 focus:border-gray-400"
            type="text"
            placeholder="İzin Ara"
          />
          <BellOutlined className="text-2xl hover:text-gray-600 transition duration-200 cursor-pointer" />
          <Dropdown menu={{ items }} placement="bottomLeft">
            <Button>PROFİL</Button>
          </Dropdown>
        </div>
      </header>

      <div className="flex flex-1 pt-[72px]">
        <aside className="w-64 bg-gray-100 shadow-lg rounded-lg p-4 pt-12">
          <nav className="flex flex-col space-y-4">
            <Link
              to="dashboard"
              className="px-4 py-2 text-sm font-medium text-gray-800 bg-gray-200 rounded-lg hover:bg-gray-300 hover:text-gray-900 transition duration-200"
            >
              DASHBOARD
            </Link>

            <Link
              to="izin-olustur"
              className="px-4 py-2 text-sm font-medium text-gray-800 bg-gray-200 rounded-lg hover:bg-gray-300 hover:text-gray-900 transition duration-200"
            >
              İzin Talebi Oluştur
            </Link>

            <Link
              to="/izin-talepleri"
              className="px-4 py-2 text-sm font-medium text-gray-800 bg-gray-200 rounded-lg hover:bg-gray-300 hover:text-gray-900 transition duration-200"
            >
              İzin Talepleri
            </Link>

            <Link
              to="/izin-onay"
              className="px-4 py-2 text-sm font-medium text-gray-800 bg-gray-200 rounded-lg hover:bg-gray-300 hover:text-gray-900 transition duration-200"
            >
              Onay Bekleyenler
            </Link>
            <Link
              to="/izin_takvimi"
              className="px-4 py-2 text-sm font-medium text-gray-800 bg-gray-200 rounded-lg hover:bg-gray-300 hover:text-gray-900 transition duration-200"
            >
              İzin Takvimi
            </Link>

            <Link
              to="/PersonelListesi"
              className="px-4 py-2 text-sm font-medium text-gray-800 bg-gray-200 rounded-lg hover:bg-gray-300 hover:text-gray-900 transition duration-200"
            >
              Personel Listesi
            </Link>

            <Link
              to="/Raporlar"
              className="px-4 py-2 text-sm font-medium text-gray-800 bg-gray-200 rounded-lg hover:bg-gray-300 hover:text-gray-900 transition duration-200"
            >
              Raporlar
            </Link>
          </nav>
        </aside>

        <main className="flex-1 p-12 h-full w-full">
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

export default HomePage;
