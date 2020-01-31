#include <iostream>
#include <string>
#include <sstream>

using namespace std;

string string_toHex(const string& input)  {
  static const char* const lut = "0123456789ABCDEF";
  size_t len = input.length();

  string output;
  output.reserve(2 * len);
  for (size_t i = 0; i < len; ++i)
  {
      const unsigned char c = input[i];
      output.push_back(lut[c >> 4]);
      output.push_back(lut[c & 15]);
  }
  return output;
}

int main() {
    string input;
    getline(cin,input);
    string hex;
    hex = string_toHex(input);
    for(int i = 0; i < 32; i++) {

    }
    return 0;
}
