#include mergesort.asm

int N = input()
array[N] array

for i = 0, i < N do
  array[i] = input()
end

mergeSort(N, array)

for i = 0, i < N do
  output(array[i])
end

