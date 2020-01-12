#include mergesort.asm

int N = input()
array[N] array

int i = 0
while i < N
  array[i] = input()
  i = i + 1
end

mergeSort(N, array)

i = 0
while i < N
  output(array[i])
  i = i + 1
end

