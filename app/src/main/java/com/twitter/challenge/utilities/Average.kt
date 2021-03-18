package com.twitter.challenge.utilities

object Average {
    /**
     * Calculates average of all values in an ArrayList<Float>.
     *
     * @param ArrayList<Float> to get average of.
     * @return Average as a float.
     */
    fun avg(list:ArrayList<Float>): Float {
        var sum = 0f
        for(num in list){
            sum+= num
        }

        return sum/list.count()
    }
}