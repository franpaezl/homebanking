import React from 'react'
import image from "../assets/upload_img_35349027_08_19_2024_08_23_09_276376_1495715083137121959.jpeg"
import LoansForm from '../components/LoansForm'

const Loans = () => {
  return (
    <div className='flex flex-col justify-center items-center mb-[50px]'>
       <h1 className='font-bold text-2xl my-[20px]'>Apply for a loan</h1>
       <div className='flex w-[80%]'>
        <LoansForm/>
        <img src={image} alt=""  className='w-[50%] h-[450px]'/>
       </div>
       
    </div>
  )
}

export default Loans
