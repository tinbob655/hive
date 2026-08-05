import React from 'react';
import './footer.scss';

export default function Footer(): React.ReactElement {

    return (
        <div id={"footer"}>
            <img src={"/logo.png"} alt={"The hive game logo"} />
            <p>
                Game made  <a href={"https://tinbob655.github.io/tinbob655/"} target={"_blank"}>Tinbob655!</a>
            </p>
        </div>
    )
}