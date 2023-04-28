# ADS_Lab2
The main goal of this laboratory work is to produce three algorithms below which provides the solution and compare their time complexity for both preparing and giving answer:
- Naive approach
- Building map (including coordinates compression)
- Segment tree (including coordinates compression)
## Complexities
|    | Naive | Map | Tree |
|----|-------|-----|------|
|Preparing complexity| O(1) | O(N^3) | O(NlogN)|
|Answering complexity| O(N^2)| O(logN) | O(logN)|
## Classes
Of course we should define some new classes to easier parse data and work with  
First one is a point. It's a class describing two point `x` and `y` both of them is `long` type due to input wavering 

