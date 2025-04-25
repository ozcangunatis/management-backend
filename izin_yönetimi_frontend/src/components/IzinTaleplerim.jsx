import React from "react";
import { useState } from 'react';
import { IoIosSearch } from "react-icons/io";
import { Button, Modal } from 'antd';

function IzinTaleplerim() {

  const [isModalOpen, setIsModalOpen] = useState(false);

  const openModel = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  const showModal = () => {
    setIsModalOpen(true);
  };

  const handleOk = () => {
    setIsModalOpen(false);
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };
  const IzinTaleplerim = [
    { id: 1, start: '13.04.2025', end: '17.04.2025', type: 'Yıllık İzin', status: 'Onaylandı' },
    { id: 2, start: '23.04.2025', end: '24.04.2025', type: 'Doğum İzni', status: 'Beklemede' },
    { id: 3, start: '08.05.2025', end: '11.05.2025', type: 'Hastalık İzni', status: 'Reddedildi' },
    { id: 4, start: '12.07.2025', end: '13.07.2025', type: 'Yıllık İzin', status: 'Beklemede' },
  ]
  const [data, setData] = useState(IzinTaleplerim);
  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-semibold">İzin Taleplerim</h1>


        <Button type="primary" onClick={showModal} className="bg-orange-500 text-white px-4 py-2 rounded-xl shadow-lg hover:bg-orange-600 flex items-center gap-2 focus:outline-none focus:ring-2 focus:ring-orange-400 transition duration-200">
          <span className="text-xl font-bold">+</span>
          Yeni İzin Talebi Oluştur
        </Button>
        <Modal
          title="İzin Oluşturma"
          centered
          open={isModalOpen} // doğru olan state
          onOk={handleOk}
          onCancel={handleCancel}
          width={1000}
        >
          <form>
            <div className="space-y-4">
              <div>
                <div className="flex flex-row gap-8">
                  <div>
                    <label
                      htmlFor="tarih"
                      className="block text-sm font-semibold"
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
                      htmlFor="tarih"
                      className="block text-sm font-semibold"
                    >
                      Bitiş Tarihi
                    </label>
                    <input
                      id="tarih"
                      type="date"
                      className="border border-gray-300 rounded-lg px-4 py-2 w-full"
                    />
                  </div>
                </div>

                <div className="w-96">
                  <label htmlFor="tur" className="block text-sm font-semibold">
                    İzin Türü Seçiniz
                  </label>
                  <select
                    id="tur"
                    className="border border-gray-300 rounded-lg px-6 py-2 w-full"
                  >
                    <option>Yıllık İzin</option>
                    <option>Mazeret İzni</option>
                    <option>Hastalık İzni</option>
                    <option>Doğum İzni</option>
                  </select>
                </div>
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


      </div  >
      <div className="flex flex-wrap gap-4 mb-4">
        <input
          className="border p-2 rounded w-48"
          type="text"
          placeholder="gg.aa.yyyy"
        />

        <select className="border p-2 rounded-xl w-48 box-border h-[42px] shadow-md focus:outline-none focus:ring-2 focus:ring-blue-400 transition duration-200">
          <option>İzin Türü Seçiniz</option>
          <option>Yıllık İzin</option>
          <option>Doğum İzni</option>
          <option>Hastalık İzni</option>
        </select>

        <select className="border p-2 rounded-xl w-48 box-border h-[42px] shadow-md focus:outline-none focus:ring-2 focus:ring-blue-400 transition duration-200">
          <option>Durum Seçiniz</option>
          <option>Beklemede</option>
          <option>Onaylandı</option>
          <option>Reddedildi</option>
        </select>

        <button className="bg-blue-500 text-white px-4 py-2 rounded-xl shadow-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400 transition duration-200 flex items-center justify-center">
          Ara
          <IoIosSearch className="ml-2" />
        </button>
      </div>


      <table className="w-full border border-gray-200 mt-6 rounded-xl overflow-hidden shadow-xl">
        <thead className="bg-gradient-to-r from-blue-100 to-blue-200">
          <tr>
            <th className="border-b px-4 py-3 text-left font-semibold text-gray-700">Başlangıç Tarihi</th>
            <th className="border-b px-4 py-3 text-left font-semibold text-gray-700">Bitiş Tarihi</th>
            <th className="border-b px-4 py-3 text-left font-semibold text-gray-700">İzin Türü</th>
            <th className="border-b px-4 py-3 text-left font-semibold text-gray-700">Durum</th>
          </tr>
        </thead>
        <tbody>
          {data.map((izin) => (
            <tr
              key={izin.id}
              className="hover:bg-blue-50 transition duration-200"
            >
              <td className="border-t px-4 py-3">{izin.start}</td>
              <td className="border-t px-4 py-3">{izin.end}</td>
              <td className="border-t px-4 py-3">{izin.type}</td>
              <td className="border-t px-4 py-3">{izin.status}</td>
            </tr>
          ))}
        </tbody>
      </table>


    </div>

  )
}

export default IzinTaleplerim;
