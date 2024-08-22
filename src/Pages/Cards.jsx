import React, { useState, useEffect } from 'react';
import DebitCreditCard from '../components/DebitCreditCard';
import { Link } from "react-router-dom";

const Cards = () => {
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
          { id: 1, amount: 1000.0, description: "Depósito", date: "2024-08-19T10:43:34.980603", type: "CREDIT" },
          { id: 2, amount: -500.0, description: "Retiro", date: "2024-08-20T10:43:34.980603", type: "DEBIT" },
          { id: 3, amount: 1500.0, description: "Depósito", date: "2024-08-19T10:43:34.980603", type: "CREDIT" }
        ]
      },
      {
        id: 2,
        accountNumber: "VIN002",
        balance: 7500.0,
        date: "2024-08-20T10:43:34.980603",
        transactions: [
          { id: 4, amount: 2000.0, description: "Depósito", date: "2024-08-19T10:43:34.980603", type: "CREDIT" },
          { id: 5, amount: -1000.0, description: "Retiro", date: "2024-08-20T10:43:34.980603", type: "DEBIT" },
          { id: 6, amount: 2000.0, description: "Depósito", date: "2024-08-19T10:43:34.980603", type: "CREDIT" }
        ]
      }
    ],
    loans: [
      { id: 1, loanId: 1, name: "hipoteca", amount: 400000.0, payments: 60 },
      { id: 2, loanId: 2, name: "personal", amount: 50000.0, payments: 12 }
    ],
    cards: [
      { id: 1, cardHolder: "Melba Morel", type: "DEBIT", color: "GOLD", number: "2454-9185-4504-6577", cvv: 610, fromDate: "08/2024", thruDate: "08/2029" },
      { id: 2, cardHolder: "Melba Morel", type: "DEBIT", color: "SILVER", number: "1727-5970-1120-8963", cvv: 815, fromDate: "08/2024", thruDate: "08/2029" },
      { id: 3, cardHolder: "Melba Morel", type: "DEBIT", color: "TITANIUM", number: "3245-6721-0987-5432", cvv: 726, fromDate: "08/2024", thruDate: "08/2029" },
      { id: 4, cardHolder: "Melba Morel", type: "CREDIT", color: "GOLD", number: "4321-5678-8765-1234", cvv: 349, fromDate: "08/2024", thruDate: "08/2029" },
      { id: 5, cardHolder: "Melba Morel", type: "CREDIT", color: "SILVER", number: "8745-3219-6472-5678", cvv: 187, fromDate: "08/2024", thruDate: "08/2029" },
      { id: 6, cardHolder: "Melba Morel", type: "CREDIT", color: "TITANIUM", number: "9087-6521-3049-1267", cvv: 763, fromDate: "08/2024", thruDate: "08/2029" }
    ]
  }]);

  const [creditCard, setCreditCard] = useState([]);
  const [debitCard, setDebitCard] = useState([]);
  
  function obtenerCards() {
    const credit = [];
    const debit = [];
    clients.forEach(client => {
      client.cards.forEach(card => {
        if (card.type === "DEBIT") {
          debit.push(card);
        } else if (card.type === "CREDIT") {
          credit.push(card);
        }
      });
    });
    setDebitCard(debit);
    setCreditCard(credit);
  }

  function colorCard(color) {
    switch (color) {
      case "GOLD":
        return "bg-[#FFD700]"; // Dorado
      case "TITANIUM":
        return "bg-[#000000]"; // Negro
      case "SILVER":
        return "bg-[#C0C0C0]"; // Plata
      default:
        return "bg-gray-200"; // Color por defecto
    }
  }

  useEffect(() => {
    obtenerCards();
  }, [clients]);

  return (
    <div className='flex flex-col w-full my-8 px-4'>
      <div className='mb-10'>
        <h2 className='text-2xl font-semibold mb-4 text-center'>Credit Cards</h2>
        <div className='flex flex-wrap w-full gap-4 justify-evenly'>
          {creditCard.map(card => (
           <div className='flex flex-col w-[30%]'>
            <h2 className='text-center font-bold'>{card.color}</h2>
            <DebitCreditCard
              key={card.id}
              cardType={card.type}
              number={card.number}
              cvv={card.cvv}
              thru={card.thruDate}
              name={card.cardHolder}
              color={colorCard(card.color)}
            />
            </div>
          ))}
        </div>
      </div>
      <div>
        <h2 className='text-2xl font-semibold mb-4 text-center'>Debit Cards</h2>
        <div className='flex flex-wrap w-full gap-4 justify-evenly'>
        {creditCard.map(card => (
           <div className='flex flex-col w-[30%]'>
            <h2 className='text-center font-bold'>{card.color}</h2>
            <DebitCreditCard
              key={card.id}
              cardType={card.type}
              number={card.number}
              cvv={card.cvv}
              thru={card.thruDate}
              name={card.cardHolder}
              color={colorCard(card.color)}
            />
            </div>
          ))}
        </div>
      </div>
      <div className='flex justify-center mt-8'>
        <Link to="/solicit-card" className="px-6 py-2 bg-[#023E73] text-white font-semibold rounded-lg shadow-md hover:bg-[#266288] focus:outline-none focus:ring-2 focus:ring-blue-400">
          Add New Card
        </Link>
      </div>
    </div>
  );
}

export default Cards;
