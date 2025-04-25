import React from "react";
import { useState } from 'react';
import { FiRefreshCcw } from "react-icons/fi";

function IzinOnay() {
  const izinVerileri = [
    {
      id: 1,
      name: 'Deniz Metin',
      dateRange: ['13-04-2025', '15-04-2025'],
      type: 'Yıllık İzin',
      status: 'Beklemede',
    },

    {
      id: 2,
      name: 'Aymina Çakır',
      dateRange: ['19-05-2025', '21-05-2025'],
      type: 'Hastalık İzni',
      status: 'Beklemede',
    },
  ]
  const [data, setData] = useState(izinVerileri);

  return (
    <div className="p-6">

      <h1 className="text-2xl font-semibold mb-6">İzin Onaylama</h1>

      <div className="flex flex-wrap gap-4 mb-4">
        <input type="text"
          placeholder="Kullanıcı Adı"
          className="border p-2 rounded-xl w-48 box-border h-[42px] shadow-md focus:outline-none focus:ring-2 focus:ring-blue-400 transition duration-200" />
        <input type="text"
          placeholder="gg.aa.yyyy"
          className="border p-2 rounded-xl w-48 box-border h-[42px] shadow-md focus:outline-none focus:ring-2 focus:ring-blue-400 transition duration-200" />
        <select className="border p-2 rounded-xl w-48 box-border h-[42px] shadow-md focus:outline-none focus:ring-2 focus:ring-blue-400 transition duration-200">
          <option>İzin Türü Seçiniz</option>
          <option>Yıllık İzin</option>
          <option>Doğum İzni</option>
          <option>Hastalık İzni</option>
        </select>
        <button className="bg-blue-500 text-white px-4 py-2 rounded-xl shadow-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400 transition duration-200 flex items-center justify-center">Yenile <FiRefreshCcw className="ml-2" /> </button>
      </div>
      <table className="w-full border border-gray-200 mt-6 rounded-xl overflow-hidden shadow-xl">
        <thead className="bg-gradient-to-r from-blue-100 to-blue-200">
          <tr>
            <th className="border-b px-4 py-3 text-left font-semibold text-gray-700">Kullanıcı Adı</th>
            <th className="border-b px-4 py-3 text-left font-semibold text-gray-700">İzin Aralığı</th>
            <th className="border-b px-4 py-3 text-left font-semibold text-gray-700">İzin Türü</th>
            <th className="border-b px-4 py-3 text-left font-semibold text-gray-700">Durum</th>
            <th className="border-b px-4 py-3 text-left font-semibold text-gray-700">İşlem</th>
          </tr>
        </thead>
        <tbody>
          {data.map((entry) => (
            <tr key={entry.id}>
              <td className="border-t px-4 py-3">{entry.name}</td>
              <td className="border-t px-4 py-3">{entry.dateRange.join('/')}</td>
              <td className="border-t px-4 py-3">{entry.type}</td>
              <td className="border-t px-4 py-3">{entry.status}</td>
              <td className="border-t px-4 py-3">
                <button className="bg-green-400 text-white px-3 py-1 rounded-xl hover:bg-green-600 mr-2">
                  Onayla
                </button>
                <button className="bg-red-400 text-white px-3 py-1 rounded-xl hover:bg-red-600">
                  Reddet
                </button>
              </td>
            </tr>

          ))}
        </tbody>
      </table>
    </div>
  )
}

export default IzinOnay;  
