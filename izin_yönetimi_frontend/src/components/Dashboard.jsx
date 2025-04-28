import React, { useEffect, useState } from "react";
import { Button, Flex, Modal } from "antd";

function Dashboard() {
  const [izinVerileri, setIzinVerileri] = useState({
    YeniIzinTalebi: 5,
    OnayBekleyenIzinler: 12,
    BuAykiIzinGunleri: 18,
    IzinKullananPersoneller: 8,
  });

  const [izinTalepleri, setIzınTalepleri] = useState([
    {
      id: 1,
      calisan: "Deniz Metin",
      tarih: "2025-04-20",
      tur: "Yıllık İzin",
      durum: "Onaylandı",
    },

    {
      id: 2,
      calisan: "Aymina Çakır",
      tarih: "2025-02-23",
      tur: "Mazeret İzni",
      durum: "Onaylandı",
    },

    {
      id: 3,
      calisan: "Tuna Bozlak",
      tarih: "2025-06-20",
      tur: "Hastalık İzni",
      durum: "Reddedildi",
    },
  ]);

  const [arama, setArama] = useState("");
  const [izinFormuAçık, setIzinFormuAçık] = useState(false);
  const [open, setOpen] = useState(false);

  useEffect(() => { }, []);

  const openModal = () => {
    setOpen(true);
  };

  const closeModal = () => {
    setOpen(false);
  };

  return (
    <div className="space-y-8">
      {/* KART YAPISI */}
      <div className="space-y-8">
        <div
          className="grid grid-cols-4
      md:grid-cols-4 gap-4"
        >
          <div className="bg-white shadow rounded-lg p-6 text-center ">
            <div className="text-lg font-semibold">Yeni İzin Talebi</div>
            <div className="text-2xl font-bold text-blue-600">
              {izinVerileri.YeniIzinTalebi}
            </div>
            <div className="text-sm text-gray-500">Bugün</div>
          </div>

          <div className="bg-white shadow rounded-lg p-6 text-center ">
            <div className="text-lg font-semibold">Onay Bekleyen İzinler</div>
            <div className="text-2xl font-bold text-blue-600">
              {izinVerileri.OnayBekleyenIzinler}
            </div>
          </div>

          <div className="bg-white shadow rounded-lg p-6 text-center ">
            <div className="text-lg font-semibold">Bu Ayki İzin Günleri</div>
            <div className="text-2xl font-bold text-blue-600">
              {izinVerileri.BuAykiIzinGunleri}
            </div>
          </div>

          <div className="bg-white shadow rounded-lg p-6 text-center ">
            <div className="text-lg font-semibold">
              İzin Kullanan Personeller
            </div>
            <div className="text-2xl font-bold text-blue-600">
              {izinVerileri.IzinKullananPersoneller}
            </div>
          </div>
        </div>
      </div>

      <div className="space-y-14">
        {/* BUTONLARIN YAPISI */}
        <div className="flex flex-row ">
          <button
            onClick={openModal}
            className=" border border-gray-400 bg-orange-400 rounded-lg px-10 py-2 "
          >
            + Yeni İzin Talebi
          </button>

          <div className="flex space-x-4 items-center ml-auto">
            <button className=" border border-gray-400 bg-zinc-200 rounded-lg px-10 py-2 ">
              Rapor Oluştur
            </button>
            <button className=" border border-gray-400 bg-blue-100 rounded-lg px-7 py-2">
              Takvim Görünümü
            </button>
          </div>
        </div>

        {/* MODAl YAPISI */}
        <Modal
          title=" Yeni İzin Oluşturma"
          centered
          open={open}
          onCancel={closeModal}
          width={800}
          bodyStyle={{
            padding: "30px",
            backgroundColor: "#f9fafb",
            borderRadius: "10px",
          }}
          footer={null}
        >
          <form className="space-y-8">
            <div className=" space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label
                    for="baslangıctarihi"
                    className="block text-sm font-semibold text-gray-700 mb-2"
                  >
                    Başlangıç Tarihi
                  </label>
                  <input
                    id="tarih"
                    type="date"
                    className="border border-gray-300 rounded-lg px-4 py-2 w-full"
                  />
                </div>

                <div>
                  <label
                    for="bitistarihi"
                    className="block text-sm font-semibold text-gray-700 mb-2"
                  >
                    Bitiş Tarihi
                  </label>
                  <input
                    id="tarih"
                    type="date"
                    className="border border-gray-300 rounded-lg px-4 py-2 w-full "
                  />
                </div>
              </div>

              <div className="md:w-[336px]">
                <label
                  for="tur"
                  className="block text-sm font-semibold text-gray-700 mb-2"
                >
                  İzin Türü Seçiniz
                </label>
                <select
                  id="tur"
                  className="border border-gray-300 rounded-lg px-4 py-2 w-full"
                  defaultValue=""
                >
                  <option value="" disabled>
                    İzin Türü Seçiniz
                  </option>
                  <option>Yıllık İzin</option>
                  <option>Mazeret İzni</option>
                  <option>Hastalık İzni</option>
                  <option>Doğum İzni</option>
                </select>
              </div>

              <div className="">
                <label
                  for="açıklama"
                  className="block text-sm font-semibold text-gray-700 mb-2"
                >
                  Açıklama Giriniz
                </label>
                <input
                  id="açıklama"
                  type="text"
                  className="border border-gray-300 rounded-lg px-4 py-2 w-full"
                />
              </div>

              <button
                type="submit"
                className="bg-blue-400 text-white px-6 py-2 rounded-lg"
              >
                OLUŞTUR
              </button>
            </div>
          </form>
        </Modal>

        {/* TABLO YAPISI */}
        <div className="overflow-x-auto">
          <div className="flex justify-between items-center mb-4 border-b pb-1">
            <h2 className="text-lg font-bold text-gray-700">
              SON İZİN TALEPLERİ
            </h2>
            <input
              type="text"
              placeholder="Personel Ara"
              value={arama}
              onChange={(e) => setArama(e.target.value)}
              className="border border-gray-300 rounded-lg px-4 py-2 "
            />
          </div>

          <table className="min-w-full mt-4 bg-white border border-gray-200 rounded-lg shadow">
            <thead className="bg-gray-100">
              <tr>
                <th className="text-left px-6 py-2 border-b"> Çalışan</th>
                <th className="text-left px-6 py-2 border-b">Tarih</th>
                <th className="text-left px-6 py-2 border-b">Tür</th>
                <th className="text-left px-6 py-2 border-b">Durum</th>
              </tr>
            </thead>

            <tbody>
              {izinTalepleri.map((talep) => (
                <tr key={talep.id} className="hover:bg-gray-50">
                  <td className="px-4 py-4 border-b">{talep.calisan}</td>
                  <td className="px-4 py-4 border-b">{talep.tarih}</td>
                  <td className="px-4 py-4 border-b">{talep.tur}</td>
                  <td className="px-4 py-4 border-b">{talep.durum}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
