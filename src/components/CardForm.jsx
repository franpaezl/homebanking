import React, { useState } from "react";
import SumbitButton from "./SumbitButton"; 

const CardForm = () => {
  const [cardType, setCardType] = useState(""); 
  const [cardColor, setCardColor] = useState("");


  function handleCardTypeChange(event) {
    setCardType(event.target.value); 
  }


  function handleCardColorChange(event) {
    setCardColor(event.target.value);
  }

  return (
    <div className="w-full h-full bg-[#93ABBF] flex">
      <form className="flex flex-col gap-[10px] justify-center items-center w-full">
        <label htmlFor="cardType" className="font-semibold text-gray-700">Select Card Type</label>
        <select
          name="cardType"
          id="cardType"
          className="w-[80%] rounded-md border-black py-1.5 pe-10 shadow-sm sm:text-sm"
          required
          value={cardType}
          onChange={handleCardTypeChange} 
        >
          <option value="" disabled>Select Card Type</option>
          <option value="credit">Credit</option>
          <option value="debit">Debit</option>
        </select>

        <label htmlFor="color" className="font-semibold text-gray-700">Select Card Color</label>
        <select
          name="color"
          id="color"
          className="w-[80%] rounded-md border-black py-1.5 pe-10 shadow-sm sm:text-sm mb-[20px]"
          required
          value={cardColor}
          onChange={handleCardColorChange} // Asocia el manejador de cambios
        >
          <option value="" disabled>Select Card Color</option>
          <option value="silver">Silver</option>
          <option value="gold">Gold</option>
          <option value="titanium">Titanium</option>
        </select>   

        <SumbitButton text="Solicit card" />
      </form>
    </div>
  );
};

export default CardForm;
