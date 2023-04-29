public class CoordinateCompressionApproach {
    int[][] map;
    Compress compressor = new Compress();
    public void buildMap(){
        compressor.makeListsX_Y(Storage.rectangles);
        map = new int[compressor.listX.length][compressor.listY.length];
        for(Rectangle rectangle: Storage.rectangles){
            int compressedLeftX=compressor.getCompressedCoordinate(compressor.listX,rectangle.leftDown.x);
            int compressedDownY= compressor.getCompressedCoordinate(compressor.listY,rectangle.leftDown.y);
            int compressedRightX= compressor.getCompressedCoordinate(compressor.listX,rectangle.rightUp.x);
            int compressedUpY= compressor.getCompressedCoordinate(compressor.listY,rectangle.rightUp.y);
            for(int i=compressedLeftX;i<compressedRightX;i++){
                for(int j=compressedDownY;j<compressedUpY;j++){
                    map[i][j]++;
                }
            }
        }
    }
    public int run(Point point){
        if(point.x<compressor.listX[0] || point.x>compressor.listX[compressor.listX.length-1] ||
                point.y < compressor.listY[0] || point.y >compressor.listY[compressor.listY.length-1]){
            return 0;
        }
        int compressedX = compressor.getCompressedCoordinate(compressor.listX, point.x);
        int compressedY = compressor.getCompressedCoordinate(compressor.listY, point.y);
        return map[compressedX][compressedY];
    }
}
