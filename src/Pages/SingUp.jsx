import React from 'react'
import "../Styles/singUp.css"
import { Link } from 'react-router-dom'; 
import image from "../assets/frnlogo.png"
import SignUpForm from '../components/SingUpForm'
import SingButton from '../components/SingButton'


const SingUp = () => {
  return (
<div className='singup flex flex-col w-full h-screen justify-center items-center'>
<div className='bg-[#93ABBF] w-[50%] flex flex-col items-center my-[20px] p-[10px]'>
  <img src={image} alt=""  className='w-[100px] h-auto' />
  <SignUpForm/>
  <SingButton text="Sing in" link="/sing-in"/>
  <p className='text-xs'>or</p>
  <Link to="/sing-in" className='text-[#333333] underline text-xs'>Sing up</Link> 



</div>

 </div>
  )
}

export default SingUp
