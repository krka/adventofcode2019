int x = 0

array[1] arr
int a = 0
int b = 0

# arr[0] references the first element of the array
# arr[1] references the array pointer
# arr[2] references the next variable - a
# arr[2] references the next next variable - b

a = 1 + 0
b = 2 + 0

x = arr[2]
output(x)

x = arr[3]
output(x)

arr[2] = 20
arr[3] = 30

output(a)
output(b)

halt
