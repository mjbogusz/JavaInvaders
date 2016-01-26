# JavaInvaders
A simple Java clone of Space Invaders game, made in MVC architecture using Observable/Observer and Swing.

## Requirements
* JDK 1.8+
* Graphical interface (compatible with Java Swing)

## Compiling
* Preferred: open project with Intellij IDEA (project was made in IDEA 15 but should work in others too) and use provided build/use target
* Alternative: compile with ant using `pl/mbogusz3/JavaInvaders/build.xml` configuration file (NOT tested but should work)

## Documentation
Compile JavaDoc with:
```javadoc -d doc pl/mbogusz3/JavaInvaders/{,*/,*/*/}*.java```
(not the best script possible but enough) then open `doc/index.html` with your preferred browser.

## Known possible improvements
* Replace Swing with lighter, more customizable framework
* Run player/enemy actions and collision detection in parallel
* Add projectile to projectile collisions
* Calculate projectile to enemy unit collision instead of looping over all units
* Fix rare case when collision detection 'switches off'

## Copyright notice
This software is licensed under MIT License:

Copyright 2016 mjbogusz

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
