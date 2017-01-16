#!/bin/bash


rm -r ../Documentation/
doxygen "Doxyfile_Android" && cp "doxy-boot.js" ../Documentation/resources/
echo "<!DOCTYPE HTML>
    <html lang='en-US'>
        <head>
            <meta http-equiv='refresh' content='0;url=resources/pages.html'>
        </head>
    </html>" >> ../Documentation/index.html
