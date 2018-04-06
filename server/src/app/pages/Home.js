import React from 'react';

const Home = () => (
    <div className='HomePage'>
        <h1 className='Page-heading'>Home</h1>
        <section className='Page-section'>
            <p>{`Local IP: ${window.ip}`}</p>
        </section>
    </div>
);

export default Home;
