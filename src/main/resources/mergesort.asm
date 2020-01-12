#include division.asm

func mergeSortInner(low, high, array, tempArray)
  int temp = 0

  int len = high - low
  if len <= 1 then
    return
  end

  int midpoint = low + div(len, 2)

  mergeSortInner(low, midpoint, array, tempArray)
  mergeSortInner(midpoint, high, array, tempArray)

  int left = low
  int right = midpoint
joinloop:
  if left == midpoint jump push_all_right
  if right == high jump push_all_left

  int leftVal = array[left]
  int rightVal = array[right]

  if leftVal > rightVal then
    tempArray[temp] = rightVal
    temp = temp + 1
    right = right + 1
  else
    tempArray[temp] = leftVal
    temp = temp + 1
    left = left + 1
  end

  jump joinloop

push_all_left:
  if left == midpoint jump copy_back
  leftVal = array[left]
  tempArray[temp] = leftVal
  temp = temp + 1
  left = left + 1
  jump push_all_left

push_all_right:
  if right == high jump copy_back
  rightVal = array[right]
  tempArray[temp] = rightVal
  temp = temp + 1
  right = right + 1
  jump push_all_right

copy_back:
  left = low
  temp = 0
copy_back_loop:
  leftVal = tempArray[temp]
  array[left] = leftVal
  left = left + 1
  temp = temp + 1
  if left < high jump copy_back_loop
end

func mergeSort(N, array)
  array[N] tempArray
  mergeSortInner(0, N, array, tempArray)
end
