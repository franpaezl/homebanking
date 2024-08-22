import React from 'react'

const SingInInput = () => {
  return (
<div className="w-full mt-[20px]">
    <form action="" className='flex flex-col gap-[10px] justify-center items-center w-full'>
        <label htmlFor="mail">Email</label>
        <input type="email" id='mail' name='mail' className="w-[80%] rounded-md border-black py-1.5 pe-10 shadow-sm sm:text-sm"/>
        <label htmlFor="password">Password</label>
        <input type="password" id='password' name='password' className="w-[80%] rounded-md border-black py-1.5 pe-10 shadow-sm sm:text-sm" />
    </form>
</div>

  )
}

export default SingInInput
