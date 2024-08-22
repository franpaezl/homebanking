import React, { useEffect, useState } from "react";
import SumbitButton from "./SumbitButton";

const TransactionsForms = () => {
  const [clients] = useState([
    {
      id: 1,
      firstName: "Melba",
      lastName: "Morel",
      email: "melbamorel@hotmail.com",
      accounts: [
        {
          id: 1,
          accountNumber: "VIN001",
          balance: 5000.0,
          date: "08/2024",
          transactions: [
            { id: 1, amount: 1000.0, description: "Depósito", date: "08/2024", type: "CREDIT" },
            { id: 2, amount: -500.0, description: "Retiro", date: "08/2024", type: "DEBIT" },
            { id: 3, amount: 1500.0, description: "Depósito", date: "08/2024", type: "CREDIT" },
          ],
        },
        {
          id: 2,
          accountNumber: "VIN002",
          balance: 7500.0,
          date: "08/2024",
          transactions: [
            { id: 4, amount: 2000.0, description: "Depósito", date: "08/2024", type: "CREDIT" },
            { id: 5, amount: -1000.0, description: "Retiro", date: "08/2024", type: "DEBIT" },
            { id: 6, amount: 2000.0, description: "Depósito", date: "08/2024", type: "CREDIT" },
          ],
        },
      ],
      loans: [
        { id: 1, loanId: 1, name: "hipoteca", amount: 400000.0, payments: 60 },
        { id: 2, loanId: 2, name: "personal", amount: 50000.0, payments: 12 },
      ],
      cards: [
        { id: 1, cardHolder: "Melba Morel", type: "DEBIT", color: "GOLD", number: "2454-9185-4504-6577", cvv: 610, fromDate: "08/2024", thruDate: "08/2029" },
        { id: 2, cardHolder: "Melba Morel", type: "DEBIT", color: "TITANIUM", number: "1727-5970-1120-8963", cvv: 815, fromDate: "08/2024", thruDate: "08/2029" },
        { id: 3, cardHolder: "Melba Morel", type: "DEBIT", color: "BLACK", number: "3245-6721-0987-5432", cvv: 726, fromDate: "08/2024", thruDate: "08/2029" },
        { id: 4, cardHolder: "Melba Morel", type: "CREDIT", color: "GOLD", number: "4321-5678-8765-1234", cvv: 349, fromDate: "08/2024", thruDate: "08/2029" },
        { id: 5, cardHolder: "Melba Morel", type: "CREDIT", color: "TITANIUM", number: "8745-3219-6472-5678", cvv: 187, fromDate: "08/2024", thruDate: "08/2029" },
        { id: 6, cardHolder: "Melba Morel", type: "CREDIT", number: "9087-6521-3049-1267", cvv: 763, fromDate: "08/2024", thruDate: "08/2029" },
      ],
    },
  ]);

  const [destinationType, setDestinationType] = useState("own");
  const [accountSelected, setAccountSelected] = useState("");
  const [availableAccounts, setAvailableAccounts] = useState([]);

  function handleChange(event) {
    setDestinationType(event.target.value);
    setAccountSelected("");
  }

  function accountSelect(event) {
    setAccountSelected(event.target.value);
  }

  useEffect(() => {
    if (destinationType === "own") {
      setAvailableAccounts(
        clients[0].accounts
          .filter(account => account.accountNumber !== accountSelected)
          .map(account => ({
            id: account.id,
            accountNumber: account.accountNumber,
          }))
      );
    } else {
      setAvailableAccounts([]);
    }
  }, [destinationType, accountSelected, clients]);

  return (
    <div className="w-[50%] bg-[#93ABBF] pt-[20px]">
      <form className="flex flex-col gap-[20px] px-[30px]">
        <h2 className="text-2xl font-bold text-gray-800">Transaction Details</h2>
        
        <div className="flex">
          <label className="font-semibold text-gray-700">Destination Type:</label>
          <div className="flex gap-4 ml-[20px]">
            <label className="flex items-center gap-2 text-gray-600">
              Own
              <input
                type="radio"
                name="destinationType"
                value="own"
                checked={destinationType === "own"}
                onChange={handleChange}
                className="form-radio h-4 w-4"
              />
            </label>
            <label className="flex items-center gap-2 text-gray-600">
              Others
              <input
                type="radio"
                name="destinationType"
                value="others"
                checked={destinationType === "others"}
                onChange={handleChange}
                className="form-radio h-4 w-4"
              />
            </label>
          </div>
        </div>

        <div className="flex flex-col">
          <label htmlFor="origen" className="font-semibold text-gray-700">Origin Account</label>
          <select
            name="origen"
            id="origen"
            onChange={accountSelect}
            value={accountSelected}
            className="mt-1 w-full rounded-lg border border-gray-300 py-2 px-3 text-gray-700 focus:ring-indigo-500 focus:border-indigo-500"
          >
            <option value="" disabled>
              Select an account
            </option>
            {clients[0].accounts.map((account) => (
              <option key={account.id} value={account.accountNumber}>
                {account.accountNumber}
              </option>
            ))}
          </select>
        </div>

        {destinationType === "own" && accountSelected && (
          <div className="flex flex-col">
            <label htmlFor="destination" className="font-semibold text-gray-700">Destination Account</label>
            <select
              name="destination"
              id="destination"
              className="mt-1 w-full rounded-lg border border-gray-300 py-2 px-3 text-gray-700 focus:ring-indigo-500 focus:border-indigo-500"
            >
              <option value="" disabled>
                Select an account
              </option>
              {availableAccounts.map((account) => (
                <option key={account.id} value={account.accountNumber}>
                  {account.accountNumber}
                </option>
              ))}
            </select>
          </div>
        )}

        {destinationType === "others" && (
          <div className="flex flex-col">
            <label htmlFor="otheraccount" className="font-semibold text-gray-700">Enter Alias or CVU</label>
            <input
              type="text"
              id="otheraccount"
              name="otheraccount"
              placeholder="Enter an account"
              className="mt-1 w-full rounded-lg border border-gray-300 py-2 px-3 text-gray-700 focus:ring-indigo-500 focus:border-indigo-500"
            />
          </div>
        )}

        <div className="flex flex-col">
          <label htmlFor="amount" className="font-semibold text-gray-700">Amount</label>
          <input
            type="number"
            id="amount"
            name="amount"
            placeholder="Enter amount"
            className="mt-1 w-full rounded-lg border border-gray-300 py-2 px-3 text-gray-700 focus:ring-indigo-500 focus:border-indigo-500"
          />
        </div>

        <div className="flex flex-col">
          <label htmlFor="description" className="font-semibold text-gray-700">Description</label>
          <textarea
            id="description"
            name="description"
            placeholder="Enter transaction description"
            className="mt-1 w-full rounded-lg border border-gray-300 py-2 px-3 text-gray-700 focus:ring-indigo-500 focus:border-indigo-500 h-32 resize-none"
          />
        </div>
      <SumbitButton text="Make Transaction"/>
      </form>
    </div>
  );
};

export default TransactionsForms;
