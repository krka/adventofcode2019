#include stdlib.asm

array[4] bits
int bitpos = 3
int hexval = 0
func readbit()
  bitpos = bitpos + 1
  if bitpos == 4 then
    bitpos = 0
    int c = read()
    if c >= '0' && c <= '9' then
      hexval = c - '0'
    else
      hexval = 10 + (c - 'A')
    end
    int t = hexval >= 8
    bits[0] = t
    if t then
       hexval = hexval - 8
    end
    t = hexval >= 4
    bits[1] = t
    if t then
       hexval = hexval - 4
    end
    t = hexval >= 2
    bits[2] = t
    if t then
       hexval = hexval - 2
    end
    bits[3] = hexval
  end
  return bits[bitpos]
end

func readbits(n)
  int res = 0
  while n do
    n = n - 1
    int x = readbit()
    res = 2 * res + x
  end
  return res
end

func literal()
  int nread = 0
  int res = 0
  int cont = 1
  while cont do
    cont = readbit()
    int v = readbits(4)
    res = res * 16 + v
    nread = nread + 5
  end
  return res, nread
end

func merge(type, a, b)
  if type == 0 then
    return a + b
  elseif type == 1 then
    return a * b
  elseif type == 2 then
    if a < b then
      return a
    else
      return b
    end
  elseif type == 3 then
    if a > b then
      return a
    else
      return b
    end
  elseif type == 5 then
    return a > b
  elseif type == 6 then
    return a < b
  end
  # assume type == 7
  return a == b
end

int sum = 0
func parse_packet()
  int version = readbits(3)
  sum = sum + version
  int type = readbits(3)
  int val = 0
  int nread = 0
  if type == 4 then
    val, nread = literal()
    nread = nread + 6
  else
    if readbit() then
      int num = readbits(11) - 1
      val, nread = parse_packet()
      while num do
        num = num - 1
        int val2, nread2 = parse_packet()
        val = merge(type, val, val2)
        nread = nread + nread2
      end
      nread = nread + 18
    else
      int len = readbits(15)
      val, nread = parse_packet()
      while nread < len do
        int val2, nread2 = parse_packet()
        val = merge(type, val, val2)
        nread = nread + nread2
      end
      nread = nread + 22
    end
  end
  return val, nread
end

int ans = parse_packet()

output(sum)
output(ans)
