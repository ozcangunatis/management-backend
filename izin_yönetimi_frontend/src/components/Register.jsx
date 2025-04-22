import React from "react";
import { UserOutlined } from "@ant-design/icons";

function Register() {
  return (
    <div
      className="w-screen flex flex-col justify-center items-center h-screen gap-4 
    bg-gradient-to-br from-gray-100 to-gray-300"
    >
      <div className="bg-gray-50 p-12 w-96 shadow-lg rounded-xl">
        <h1 className="text-2xl mb-4 text-center font-semibold text-gray-900">
          KAYIT OL
        </h1>
        <div className="flex justify-center items-center mb-4">
          <UserOutlined className="text-5xl text-gray-700" />
        </div>

        <form className="flex flex-col space-y-3 mb-4">
          <input
            className="border border-gray-300 rounded-md px-4 py-2 focus:outline-none focus:ring-2 focus:ring-gray-400"
            type="text"
            placeholder="Kullanıcı Adı Giriniz :"
            required
          />
          <input
            className="border border-gray-300 rounded-md px-4 py-2 focus:outline-none focus:ring-2 focus:ring-gray-400"
            type="e-mail"
            placeholder="E-mail Giriniz :"
            required
          />
          <input
            className="border border-gray-300 rounded-md px-4 py-2 focus:outline-none focus:ring-2 focus:ring-gray-400"
            type="Password"
            placeholder="Parola Giriniz :"
            required
          />
          <input
            className="border border-gray-300 rounded-md px-4 py-2 focus:outline-none focus:ring-2 focus:ring-gray-400"
            type="ConfirmPassword"
            placeholder="Parola Tekrar Giriniz :"
            required
          />
        </form>

        <div className="flex items-center mb-4">
          <input
            className="w-4 h-4 text-gray-700 border-gray-600 rounded-2xl focus:ring-gray-400 focus:ring-2"
            type="checkbox"
            name="accepterms"
            required
          />
          <label className="ml-2 text-ml text-gray-700">Beni Hatırla</label>
        </div>

        <div className="mb-4 flex flex col items-center">
          <button className="bg-gray-700 text-white px-6 py-2 rounded-md hover:bg-gray-800 transition duration-200 w-full">
            KAYIT OL
          </button>
        </div>
        <div>
          <span className="text-gray-700">
            Zaten bir hesabınız var mı?{" "}
            <a href="#" className="text-blue-500 hover:underline">
              {" "}
              Giriş Yap{" "}
            </a>
          </span>
        </div>
      </div>
    </div>
  );
}

export default Register;
