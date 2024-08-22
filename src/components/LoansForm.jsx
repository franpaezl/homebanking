import React, { useState } from 'react';
import { Link } from "react-router-dom";
import SubmitButton from "./SumbitButton";

const LoansForm = () => {

  const [clients, setClients] = useState([{
    id: 1,
    firstName: "Melba",
    lastName: "Morel",
    email: "melbamorel@hotmail.com",
    accounts: [
      {
        id: 1,
        accountNumber: "VIN001",
        balance: 5000.0,
        date: "2024-08-19T10:43:34.980603",
        transactions: [
          {
            id: 1,
            amount: 1000.0,
            description: "Depósito",
            date: "2024-08-19T10:43:34.980603",
            type: "CREDIT"
          },
          {
            id: 2,
            amount: -500.0,
            description: "Retiro",
            date: "2024-08-20T10:43:34.980603",
            type: "DEBIT"
          },
          {
            id: 3,
            amount: 1500.0,
            description: "Depósito",
            date: "2024-08-19T10:43:34.980603",
            type: "CREDIT"
          }
        ]
      },
      {
        id: 2,
        accountNumber: "VIN002",
        balance: 7500.0,
        date: "2024-08-20T10:43:34.980603",
        transactions: [
          {
            id: 4,
            amount: 2000.0,
            description: "Depósito",
            date: "2024-08-19T10:43:34.980603",
            type: "CREDIT"
          },
          {
            id: 5,
            amount: -1000.0,
            description: "Retiro",
            date: "2024-08-20T10:43:34.980603",
            type: "DEBIT"
          },
          {
            id: 6,
            amount: 2000.0,
            description: "Depósito",
            date: "2024-08-19T10:43:34.980603",
            type: "CREDIT"
          }
        ]
      }
    ],
    loans: [
      {
        id: 1,
        loanId: 1,
        name: "hipoteca",
        amount: 400000.0,
        payments: 60
      },
      {
        id: 2,
        loanId: 2,
        name: "personal",
        amount: 50000.0,
        payments: 12
      }
    ],
    cards: [
      {
        id: 1,
        cardHolder: "Melba Morel",
        type: "DEBIT",
        color: "GOLD",
        number: "2454-9185-4504-6577",
        cvv: 610,
        fromDate: "2024-08-19T10:43:34.980603",
        thruDate: "2029-08-19T10:43:34.980603"
      },
      {
        id: 2,
        cardHolder: "Melba Morel",
        type: "DEBIT",
        color: "TITANIUM",
        number: "1727-5970-1120-8963",
        cvv: 815,
        fromDate: "2024-08-19T10:43:34.980603",
        thruDate: "2029-08-19T10:43:34.980603"
      }
    ]
  }]);

  const [loanType, setLoanType] = useState('Mortgage');

  const loanOptions = {
    Mortgage: { maxAmount: 500000, payments: [12, 24, 36, 48, 60] },
    Personal: { maxAmount: 50000, payments: [6, 12, 24] },
    Automotive: { maxAmount: 30000, payments: [12, 24, 36] },
  };

  const handleLoanTypeChange = (e) => {
    setLoanType(e.target.value);
  };

  const { maxAmount, payments } = loanOptions[loanType];

  return (
    <div className="w-[50%] px-[20px] bg-[#93ABBF] pt-[20px]">
      <h2 className="text-2xl font-bold text-gray-800 mb-4">Select Loan</h2>
      <form className="flex flex-col gap-[20px] px-[30px]">
        
        <div className="flex flex-col">
          <label htmlFor="loanType" className="font-semibold text-gray-700">Loan Type</label>
          <select
            id="loanType"
            value={loanType}
            onChange={handleLoanTypeChange}
            className="mt-1 w-full rounded-lg border border-gray-300 py-2 px-3 text-gray-700 focus:ring-indigo-500 focus:border-indigo-500"
          >
            {Object.keys(loanOptions).map((type) => (
              <option key={type} value={type}>
                {type}
              </option>
            ))}
          </select>
        </div>

        <div className="flex flex-col">
          <label htmlFor="amount" className="font-semibold text-gray-700">Amount</label>
          <input
            type="number"
            id="amount"
            name="amount"
            max={maxAmount}
            placeholder={`Enter amount (max ${maxAmount})`}
            className="mt-1 w-full rounded-lg border border-gray-300 py-2 px-3 text-gray-700 focus:ring-indigo-500 focus:border-indigo-500"
          />
        </div>

        <div className="flex flex-col">
          <label htmlFor="payments" className="font-semibold text-gray-700">Payments</label>
          <select
            id="payments"
            name="payments"
            className="mt-1 w-full rounded-lg border border-gray-300 py-2 px-3 text-gray-700 focus:ring-indigo-500 focus:border-indigo-500"
          >
            {payments.map((payment) => (
              <option key={payment} value={payment}>
                {payment} payments
              </option>
            ))}
          </select>
        </div>
        <SubmitButton text="Solicit Loan"/>

      </form>
    </div>
  );
};

export default LoansForm;
