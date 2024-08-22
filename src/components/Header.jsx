import React from 'react';
import Navbar from './Navbar';
import { Link } from 'react-router-dom';
import image from "../assets/frnlogo.png"

const Header = () => {
  return (
    <header className='flex flex-row justify-evenly items-center bg-[#93ABBF]'>
      <img src={image} alt="Bank Icon" className="w-[7%] my-[10px]" />
      <Navbar />
      <Link to="/sing-in">
        <i className='fa-solid fa-right-from-bracket text-2xl w-[25%  ]'></i>
      </Link>
    </header>
  );
}

export default Header;
