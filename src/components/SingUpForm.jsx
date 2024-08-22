import React from "react";

const SignUpForm = () => {
  return (
    <div className="w-full mt-[20px]">
      <form
        action=""
        className="flex flex-col gap-[10px] justify-center items-center w-full"
      >
        <label htmlFor="firstname">First Name</label>
        <input
          type="text"
          id="firstname"
          name="firstname"
          className="w-[80%] rounded-md border-black py-1.5 pe-10 shadow-sm sm:text-sm"
          required
        />

        <label htmlFor="lastname">Last Name</label>
        <input
          type="text"
          id="lastname"
          name="lastname"
          className="w-[80%] rounded-md border-black py-1.5 pe-10 shadow-sm sm:text-sm"
          required
        />

        <label htmlFor="email">Email</label>
        <input
          type="email"
          id="email"
          name="email"
          className="w-[80%] rounded-md border-black py-1.5 pe-10 shadow-sm sm:text-sm"
          required
        />

        <label htmlFor="password">Password</label>
        <input
          type="password"
          id="password"
          name="password"
          className="w-[80%] rounded-md border-black py-1.5 pe-10 shadow-sm sm:text-sm"
          required
        />
      </form>
    </div>
  );
};

export default SignUpForm;
