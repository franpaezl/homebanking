import React from "react";
import { Link } from "react-router-dom"; // Asegúrate de importar Link aquí

const SingButton = (props) => {
  return (
    <div className="flex justify-center mt-[20px]">
      <Link
        to={props.link}
        className="px-6 py-2 bg-[#023E73] text-white font-semibold rounded-lg shadow-md hover:bg-[#266288] focus:outline-none focus:ring-2 focus:ring-blue-400"
      >
        {props.text}
      </Link>
    </div>
  );
};

export default SingButton;
