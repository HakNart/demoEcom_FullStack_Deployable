import React from 'react'
import { Accordion } from "./Accordion";

export const Faq = () => {
    const faqs = [
        {
          "id": 1,
          "question": "Why should I use KT Store?",
          "answer": "KT Store is the best choice for all your kitchen equipment needs because we offer a wide range of high-quality products at competitive prices. Our team of experts is always available to offer you advice, and our online store is open 24/7 for convenient shopping. We also offer fast and reliable delivery to your doorstep, so you can start using your new equipment as soon as possible. Our customer service team is dedicated to providing you with the best possible shopping experience, and we are always here to help if you have any questions or concerns. Choose KT Store for a hassle-free and enjoyable shopping experience!"
        },
        {
          "id": 2,
          "question": "How can I contact for support?",
          "answer": "If you need support or have any questions or concerns, there are several ways to get in touch with us at KT Store. You can fill out the contact form on our website, email us at support@ktstore.com, call our customer service hotline at 1-800-555-KTST (1-800-555-5878), or chat with us live on our website. Our friendly and knowledgeable customer service team is always happy to assist you with any inquiries you may have. We are committed to providing exceptional support to ensure your satisfaction with our products and services. Contact us today for assistance!"
        },
        {
          "id": 3,
          "question": "Do you offer refunds?",
          "answer": "KT Store offers a refund policy for our customers. If you are not satisfied with your purchase for any reason, you may return it within 30 days of receipt for a full refund. The item must be unused and in its original packaging to be eligible for a refund. Please note that some items may be subject to a restocking fee. To initiate a return, please contact our customer service team via our contact form, email, phone, or live chat, and they will guide you through the process. At KT Store, we strive to provide the best possible shopping experience, and we stand behind the quality of our products. Contact us if you have any questions or concerns about our refund policy."
        },
        {
          "id": 4,
          "question": "Do you support Online payments?",
          "answer": "Yes, KT Store supports online payments for your convenience and security. We accept a variety of payment methods, including major credit cards such as Visa, Mastercard, and American Express, as well as PayPal and Apple Pay. Our online payment system is secure and encrypted to protect your personal and financial information. When you place an order on our website, you can rest assured that your payment will be processed safely and securely. If you have any questions or concerns about our online payment system, please don't hesitate to contact our customer service team for assistance. We are always happy to help!"
        }
    ];
    
  return (
    <section className="my-10 p-7 border rounded dark:border-slate-700 shadow-sm">        
      <h1 className="text-2xl text-center font-semibold dark:text-slate-100 mb-3 underline underline-offset-8">Question in mind?</h1>    
            <div className="" id="accordion-flush" data-accordion="collapse" data-active-classes="bg-white dark:bg-gray-900 text-gray-900 dark:text-white" data-inactive-classes="text-gray-500 dark:text-gray-400">
              { faqs.map((faq) => (
                <Accordion key={faq.id} faq={faq} /> 
              )) }
            </div>
      </section>
  )
}
