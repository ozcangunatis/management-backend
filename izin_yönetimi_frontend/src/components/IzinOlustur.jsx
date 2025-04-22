import React from "react";
import { DatePicker, Space } from "antd";

const onChange = (date, dateString) => {
  console.log(date, dateString);
};

function IzinOlustur() {
  return (
    <div>
      <h1>İZİN TALEBİ OLUŞTUR</h1>

      <div className="flex flex-row gap-15">
        <div className="flex flex-col gap-3">
          <div>Başlangıç Tarihi:</div>
          <DatePicker onChange={onChange} />
        </div>

        <div className="flex flex-col gap-3">
          <div>Bitiş Tarihi:</div>
          <DatePicker onChange={onChange} />
        </div>
      </div>
    </div>
  );
}

export default IzinOlustur;
