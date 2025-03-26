import React, { useState, useEffect } from "react";
import axios from "axios";
const API_URL = "http://localhost:8080/api/speakers";

const SpeakerOffersPage = () => {
  const [offers, setOffers] = useState([]);
  const speakerId = localStorage.getItem("userId");

  const fetchOffers = async () => {
    try {
      const response = await axios.get(`${API_URL}/get-all-offers/${speakerId}`);
      setOffers(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchOffers();
  }, [speakerId]);

  useEffect(() => {
    console.log("Updated Offers State:", offers);
  }, [offers]);

  const handleOfferUpdate = async (offerId, status) => {
    try {
      const response = await axios.post(`${API_URL}/update-speaker-offer`, {
        offerId,
        status,
      });
      if (response.status === 200) {
        fetchOffers();
      }
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="flex flex-col items-center mt-6">
      <h2 className="text-2xl font-bold pt-2">Your Offers</h2>
      <p className="text-gray-600 mt-2">
        Here are the offers you have received:
      </p>

      <hr className="my-10 w-full max-w-6xl border-t border-gray-300" />

      {offers.length > 0 ? (
        <div className="w-full max-w-6xl grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {offers.map((offer) => (
            <div key={offer.id} className="border border-[#D9C2A3] bg-white p-4 rounded-lg shadow flex flex-col justify-between">
              <h3 className="font-bold text-lg text-[#2E2E2E]">Offer {offer.id}</h3>
              <p className="text-sm text-gray-700 mt-1">
                Status: {offer.status}
              </p>
              {offer.status === "PENDING" && (
                <div className="flex justify-between mt-4">
                  <button
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                    onClick={() => handleOfferUpdate(offer.id, "ACCEPTED")}
                  >
                    Accept
                  </button>
                  <button
                    className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                    onClick={() => handleOfferUpdate(offer.id, "DECLINED")}
                  >
                    Decline
                  </button>
                </div>
              )}
            </div>
          ))}
        </div>
      ) : (
        <p className="text-gray-500 mt-4">No offers found.</p>
      )}
    </div>
  );
};

export default SpeakerOffersPage;