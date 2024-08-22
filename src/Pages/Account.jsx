import axios from "axios";
import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import image from "../assets/pikaso_texttoimage_35mm-film-photography-necesito-crear-una-imagen-pa.jpeg";
import AccountsCards from "../components/AccountsCards";
import TransactionsTables from "../components/TransactionsTable";

const Account = () => {
  const [accounts, setAccounts] = useState({});
  const [accountTransactions, setAccountTransactions] = useState([]);

  const { id } = useParams();

  function obtenerAccounts() {
    axios
      .get(`http://localhost:8080/api/account/` + id)
      .then((response) => {
        let account = response.data;
        setAccounts(account);
        setAccountTransactions(account.transactions || []);
      })
      .catch((error) => {
        console.error("Hubo un error al obtener los datos de la cuenta!", error);
      });
  }

  function formatDate(dateString) {
    const date = new Date(dateString);
    const options = { year: "numeric", month: "numeric" };
    return date.toLocaleDateString("en-US", options);
  }

  function formatAmountToARS(amount) {
    return new Intl.NumberFormat("es-AR", {
      style: "currency",
      currency: "ARS",
    }).format(amount);
  }

  function transactionTypeColor(type) {
    switch (type) {
      case "DEBIT":
        return "text-red-500";
      case "CREDIT":
        return "text-green-500";
      default:
        return "text-gray-500";
    }
  }

  useEffect(() => {
    obtenerAccounts();
  }, [id]);

  return (
    <div>
      <h1 className="font-bold text-2xl my-[20px] text-center">
        Tu cuenta seleccionada: {accounts.accountNumber}
      </h1>
      <img
        src={image}
        alt="homeb-img"
        className="h-[400px] w-[100%] pt-[20px] object-cover"
      />
      <div className="flex my-[20px] justify-between">
        <AccountsCards
          id={accounts.id}
          number={accounts.accountNumber}
          amount={formatAmountToARS(accounts.balance)}
          date={formatDate(accounts.date)}
        />
        <div className="w-[60%]">
          <h2 className="text-center text-xl font-semibold mb-4">Transactions</h2>
          <table className="w-full bg-white border border-gray-200 rounded-lg shadow-md mt-4">
            <thead className="bg-gray-100 border-b border-gray-200">
              <tr>
                <th className="py-2 px-4 text-left text-gray-600 font-semibold">
                  Tipo
                </th>
                <th className="py-2 px-4 text-left text-gray-600 font-semibold">
                  Monto
                </th>
                <th className="py-2 px-4 text-left text-gray-600 font-semibold">
                  Fecha
                </th>
                <th className="py-2 px-4 text-left text-gray-600 font-semibold">
                  Descripción
                </th>
              </tr>
            </thead>
            <tbody>
              {accountTransactions.map((transaction) => (
                <TransactionsTables
                  color={transactionTypeColor(transaction.type)}
                  type={transaction.type}
                  amount={formatAmountToARS(transaction.amount)}
                  date={formatDate(transaction.date)}
                  description={transaction.description}
                />
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default Account;
