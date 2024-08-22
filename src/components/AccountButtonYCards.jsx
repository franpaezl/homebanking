import React, { useState, useEffect } from "react";
import axios from "axios";
import RequestButton from "./RequestButton";
import AccountsCards from "./AccountsCards";

const AccountButtonYCards = () => {
  const [accounts, setAccounts] = useState([]);

  function obtenerClientes() {
    axios
      .get("http://localhost:8080/api/clients/")
      .then((response) => {
        let clients = response.data;
        if (clients.length > 0) {
          let moraAccounts = clients[0].accounts;
          setAccounts(moraAccounts);
        } else {
          setAccounts([]);
        }
      })
      .catch((error) => {
        console.error("Error fetching clients:", error);
      });
  }

  useEffect(() => {
    obtenerClientes();
  }, []);

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

  return (
    <div className="flex w-full my-[20px]">
      <RequestButton />
      <div className="flex w-[100%] gap-[50px] items-center">
        {accounts.map((account) => (
          <AccountsCards
            id={account.id}
            number={account.accountNumber}
            amount={formatAmountToARS(account.balance)}
            date={formatDate(account.date)}
          />
        ))}
      </div>
    </div>
  );
};

export default AccountButtonYCards;
