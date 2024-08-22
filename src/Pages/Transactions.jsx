import React from "react";
import TransactionsForm from "../components/TransactionsForms";
import image from "../assets/upload_img_35349027_08_20_2024_14_03_29_890336_4628348994104959729.jpeg"


const Transactions = () => {
  return (
    <div>
      <div className="flex flex-col justify-center items-center mb-[50px]">
        <h1 className="font-bold text-2xl my-[20px]">Transactions</h1>
        <div className="flex w-[80%] h-[700px] ">
          <TransactionsForm/>
          <img src={image} alt="" className="w-[50%]" />
        </div>
      </div>
    </div>
  );
};

export default Transactions;
