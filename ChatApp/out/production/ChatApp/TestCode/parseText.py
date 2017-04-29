reading = open("SendText.txt", 'r')
text = reading.read()
reading.close()

test = "".join(text.split("\n"))
# print(test)
# test = "".join(test.split(" "))
final = ""
for item in test.split(" "):
    if " " not in item and "\n" not in item and item != " ":
        item = "".join(item.split())
        if item != "":
            final += item + " "
# print(final)

writing = open("NewText.txt", 'w')
writing.write(final)
writing.close()
