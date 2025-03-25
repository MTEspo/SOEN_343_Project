// Frontend/src/promotion/observer/EmailNotifier.js

import axios from "axios";

export class EmailNotifier {
  async update(eventData) {
    const payload = {
      subject: `Promotion for ${eventData.name}`,
      text: `
        <p>Hello,</p>
        <p>We're excited to announce our upcoming event: <strong>${eventData.name}</strong>.</p>
        <p><strong>Type:</strong> ${eventData.type}<br/>
           <strong>Price:</strong> $${parseFloat(eventData.price).toFixed(2)}</p>
        <p>${eventData.description}</p>
        <p>Register now and don’t miss out!</p>
      `,
    };

    try {
      const res = await axios.post(
        "http://localhost:8080/api/organizer/email-promote",
        payload,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      console.log(`📧 Email promotion sent for: ${eventData.name}`);
    } catch (err) {
      console.error(`❌ Failed to send email promotion for ${eventData.name}:`, err.message);
    }
  }
}
