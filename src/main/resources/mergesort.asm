#include division.asm

func mergeSortInner(low, high, array, tempArray)
  int temp = 0

  int len = high - low
  if len <= 1 then
    return
  end

  int midpoint = low + len / 2

  mergeSortInner(low, midpoint, array, tempArray)
  mergeSortInner(midpoint, high, array, tempArray)

  int left = low
  int right = midpoint

  while temp < high do
    int leftVal = array[left]
    int rightVal = array[right]

    if left == midpoint then
      while right < high do
        rightVal = array[right]
        tempArray[temp] = rightVal
        temp = temp + 1
        right = right + 1
      end
      break
    elseif right == high then
      while left < midpoint do
        leftVal = array[left]
        tempArray[temp] = leftVal
        temp = temp + 1
        left = left + 1
      end
      break
    else
      if leftVal > rightVal then
        tempArray[temp] = rightVal
        temp = temp + 1
        right = right + 1
      else
        tempArray[temp] = leftVal
        temp = temp + 1
        left = left + 1
      end
    end
  end

  left = low
  temp = 0
  while left < high do
    array[left] = tempArray[temp]
    left = left + 1
    temp = temp + 1
  end
end

func mergeSort(N, array)
  array[N] tempArray
  mergeSortInner(0, N, array, tempArray)
end
