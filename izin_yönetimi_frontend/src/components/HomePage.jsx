import { BellOutlined, EditOutlined } from "@ant-design/icons";
import { Button, Dropdown, Space, Tooltip } from "antd";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import IzinOlustur from "./IzinOlustur";
import IzinTakvimi from "./IzinTakvimi";
import IzinOnay from "./IzinOnay";
import IzinTaleplerim from "./IzinTaleplerim";
import Dashboard from "./Dashboard";
import { useEffect, useState } from "react";
import { Flex, Modal } from "antd";
import { Switch } from "antd";

function HomePage() {
  const [theme, setTheme] = useState(localStorage.getItem("theme") || "light");

  useEffect(() => {
    if (
      localStorage.theme === "dark" ||
      (!("theme" in localStorage) &&
        window.matchMedia("(prefers-color-scheme: dark)").matches)
    ) {
      document.documentElement.classList.add("dark");
    } else {
      document.documentElement.classList.remove("dark");
    }
  }, []);

  const changeTheme = () => {
    setTheme(theme === "light" ? "dark" : "light");

    if (theme === "dark") {
      document.documentElement.classList.add("dark");
      localStorage.setItem("theme", "dark");
    } else {
      document.documentElement.classList.remove("dark");
      localStorage.setItem("theme", "light");
    }
  };

  const items = [
    {
      key: "1",
      label: <button onClick={() => setOpen(true)}> Ayarlar</button>,
    },

    {
      key: "4",
      label: <a href="/">Çıkış</a>,
    },
  ];

  const [open, setOpen] = useState(false);
  const [name, setName] = useState("Deniz Metin ");
  const [mail, setMail] = useState("denizmetin@gmail.com");

  return (
    <div className="flex flex-col h-screen bg-gray-50 dark:bg-gray-700 text-gray-800 dark:text-gray-500">
      <header className="fixed top-0 left-0 w-full flex items-center justify-between p-4 bg-gradient-to-r shadow-md z-10 from-gray-200 dark:from-gray-400 to-gray-400  text-gray-900  ">
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
            <Button>{name}</Button>
          </Dropdown>
        </div>
      </header>

      <div className="flex flex-1 pt-[72px]">
        <aside className="w-64 bg-gray-100 shadow-lg rounded-lg p-4 pt-12 dark:bg-gray-900">
          <nav className="flex flex-col space-y-4">
            <Link
              to="dashboard"
              className="px-4 py-2 text-sm font-medium text-gray-800 bg-gray-200 rounded-lg hover:bg-gray-300 hover:text-gray-900 transition duration-200"
            >
              DASHBOARD
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
            <Route path="/" element={<Dashboard />} />
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/izin-olustur" element={<IzinOlustur />} />
            <Route path="/izin-talepleri" element={<IzinTaleplerim />} />
            <Route path="/izin-onay" element={<IzinOnay />} />
            <Route path="/izin_takvimi" element={<IzinTakvimi />} />
          </Routes>
        </main>
      </div>

      <Modal
        title=" PROFİL AYARLARI"
        centered
        open={open}
        onOk={() => setOpen(false)}
        onCancel={() => setOpen(false)}
        width={600}
      >
        <div>
          <div className="flex flex-col gap-4">
            <div className="bg-neutral-100 dark:bg-gray-800 rounded-xl shadow-md p-6 space-y-4">
              <h3 className="text-lg font-medium text-gray-800 dark:text-gray-400 mb-4">
                Profil Bilgileri
              </h3>

              <div className="space-y-3">
                <div className="flex items-center justify-between">
                  <label className="text-sm font-medium text-gray-700 dark:text-gray-400">
                    Kullanıcı Adı
                  </label>

                  <input
                    id="name"
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    className="w-2/3 px-3 py-2 border border-gray-300 rounded-md bg-gray-50 dark:bg-gray-400"
                  />
                </div>
              </div>
              <div>
                <div className="flex items-center justify-between ">
                  <label className="text-sm font-medium text-gray-700 dark:text-gray-400">
                    E-posta Güncelle
                  </label>
                  <input
                    id="mail"
                    type="text"
                    value={mail}
                    onChange={(e) => setMail(e.target.value)}
                    className="w-2/3 px-3 py-2 border border-gray-300 rounded-md bg-gray-50 dark:bg-gray-400"
                  />
                </div>
              </div>
            </div>
            <div className="bg-neutral-100 dark:bg-gray-800 rounded-xl shadow-md p-6 space-y-4">
              <div className="flex items-center justify-between">
                <label className="text-lg font-medium text-gray-700 dark:text-gray-400">
                  Tema
                </label>
                <Switch
                  onChange={changeTheme}
                  checked={theme === "dark"}
                  checkedChildren="Karanlık"
                  unCheckedChildren="Açık"
                />
              </div>
            </div>

            <div className="bg-neutral-100 dark:bg-gray-800 rounded-xl shadow-md p-6 space-y-4 px-4 py-4">
              <div className="flex flex-row items-center justify-between">
                <h3 className="text-lg font-medium text-gray-700 dark:text-gray-400 mb-4">
                  Dil Seçimi
                </h3>
                <select
                  className="w-1/3 px-3 py-2 border border-gray-300 rounded-md bg-gray-50 dark:bg-gray-500 dark:text-gray-300"
                  defaultValue="tr"
                >
                  <option value="tr">Türkçe</option>
                  <option value="en">English</option>
                </select>
              </div>
            </div>

            <div>
              <div className="bg-neutral-100 dark:bg-gray-800 rounded-xl shadow-md space-y-4 ">
                <div className="flex flex-row items-center justify-between p-5">
                  <h3 className="text-lg font-medium text-gray-700 dark:text-gray-300">
                    Şifreyi Değiştir
                  </h3>
                  <button
                    className="w-1/3 px-4 py-2 bg-blue-500 hover:bg-blue-600 text-white font-semibold rounded-lg transition focus:outline-none focus:ring-2 focus:ring-blue-400"
                    onClick={() => alert("şifre işlemleri başlatıldı")}
                  >
                    Şifreyi Değiştir
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </Modal>
    </div>
  );
}

export default HomePage;
