import React from 'react';
import { Link } from 'react-router-dom'; 
import image from "../assets/upload_img_17452219_08_16_2024_10_54_42_474900_6983826868170718299.jpeg";
import image2 from "../assets/frnlogo.png"
import SingInInput from '../components/SingInInput';
import SingButton from '../components/SingButton';



const SingIn = () => {
  return (
    <div className='flex'>
      <img src={image} alt="Background" className='w-[60%] h-screen object-cover' />
      <div className='bg-[#93ABBF] flex flex-col justify-center items-center w-[40%] p-4 '>
        <img src={image2} alt="Logo" className='w-[100px] h-auto' /> 
        <SingInInput />
        <SingButton link="/sing-up" text= "Login"/ >
        <p className='text-xs'>o</p>
        <Link to="/sing-up" className='text-[#333333] underline text-xs'>Sing up</Link> 
      </div>
    </div>
  );
};

export default SingIn;
