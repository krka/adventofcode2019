#include division.asm

func mergeSortInner(low, high, array, tempArray)
  int len = 0
  int halfLen = 0

  int midpoint = 0

  int leftVal = 0
  int rightVal = 0
  int left = 0
  int right = 0

  int temp = 0

  len = high - low
  if len <= 1 jump finish

  halfLen = div(len, 2)
  midpoint = low + halfLen

  mergeSortInner(low, midpoint, array, tempArray)
  mergeSortInner(midpoint, high, array, tempArray)

  left = low
  right = midpoint
joinloop:
  if left == midpoint jump push_all_right
  if right == high jump push_all_left

  leftVal = array[left]
  rightVal = array[right]

  if leftVal > rightVal jump push_right
push_left:
  tempArray[temp] = leftVal
  temp = temp + 1
  left = left + 1
  jump joinloop

push_right:
  tempArray[temp] = rightVal
  temp = temp + 1
  right = right + 1
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

finish:
  return
endfunc

func mergeSort(N, array)
  array[N] tempArray
  mergeSortInner(0, N, array, tempArray)
  return
endfunc
