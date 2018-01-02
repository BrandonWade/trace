import React, { Component } from 'react';

class Home extends Component {
    render() {
        return (
            <div className={'HomePage'}>
                <h1 className={'Page-heading'}>Home</h1>
                <section className={'Page-section'}>
                    <p>{`Local IP: ${window.ip}`}</p>
                </section>
            </div>
    );
    }
};

export default Home;
