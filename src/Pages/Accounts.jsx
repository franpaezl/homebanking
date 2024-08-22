import { useEffect, useState } from "react";
import image from "../assets/pikaso_texttoimage_35mm-film-photography-necesito-crear-una-imagen-pa.jpeg";
import AccountButtonYCards from '../components/AccountButtonYCards';
import axios, { all } from "axios";

const Accounts = () => {
  const [clients, setClients] = useState([]);

  function obtenerClientes() {
    axios.get("http://localhost:8080/api/clients/")
      .then(response => {
        let allClients = response.data
        if (allClients.length > 0){
          setClients(allClients)
        }
        else
        setClients([])


      })
      .catch(error => {
        setError(error);

      });
  }

  useEffect(() => {
    obtenerClientes();
  }, []);


  return (
    <div className="flex flex-col justify-center items-center pt-[20px]">
        {clients.length>0 &&(<h1 className="text-4xl font-bold">Welcome {clients[0].firstName}</h1>)}
        

      <img
        src={image} 
        alt="homeb-img"
        className="h-[400px] w-[100%] pt-[20px] object-cover"
      />
      <AccountButtonYCards />
    </div>
  );
};

export default Accounts;
