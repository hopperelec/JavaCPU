# This is a Python equivalent of bubbleSort.txt, for demonstration purposes

items = []

nextAddressIn = 0
# -inputNextNumber
while True:
#     INP 1 10
    accumulator = int(input("Next integer: "))
#       BAD -startSorting
    if accumulator < 0:
        break
#       STA $nextAddressIn
    items.append(accumulator)
#     LDA $nextAddressIn
#       INC 1
#       STO $nextAddressIn
    nextAddressIn += 1
#     BAA -inputNextNumber
    continue

# -startSorting
#     LDA $dynamicLoadFirstItem
#       INC 1
#       STO $dynamicLoadFirstItem
#     LDA $dynamicLoadSecondItem
#       INC 1
#       STO $dynamicLoadSecondItem

#     -sortWhile
while True:
#         LDA $one
#           STO $isSorted
    isSorted = True
#         LDA $zero
#           STO $i
#         -sortFor
#             LDA $end
#               ADD $i
#               SUB $nextAddressIn
#               BAC -endSortFor
    for i in range(len(items)-1):
#             LDA $end
#               ADD $i
#               STO $firstAddress
#               STA $dynamicLoadFirstItem
#               INC 1
#               STO $secondAddress
#               STA $dynamicLoadSecondItem
#             LDA $i
#               INC 1
#               STO $i
#             -dynamicLoadFirstItem
#             LDA 0
#               STO $firstItem
#             -dynamicLoadSecondItem
#             LDA 0
#               STO $secondItem
        firstItem = items[i]
        secondItem = items[i+1]
#               SUB $firstItem
#               DEC 1
#               BAD -sortFor
        if secondItem <= firstItem:
            continue
#             LDA $firstItem
#               STA $secondAddress
        items[i+1] = firstItem
#             LDA $secondItem
#               STA $firstAddress
        items[i] = secondItem
#             LDA $zero
#               STO $isSorted
        isSorted = False
#             BAA -sortFor
        continue
#         -endSortFor
#         LDA $isSorted
#           SUB $one
    if isSorted:
#           BAC -startOutputting
        break
#         BAA -sortWhile
    continue

# -startOutputting
# LDA $dynamicLoadOutputItem
#   INC 1
#   STO $dynamicLoadOutputItem

# LDA $zero
#   STO $i
# -outputNextNumber
#     LDA $end
#       ADD $i
#       SUB $nextAddressIn
#       BAC -halt
for i in range(len(items)):
#     LDA $end
#       ADD $i
#       STA $dynamicLoadOutputItem
#     -dynamicLoadOutputItem
#     LDA 0
#       OUT 10
    print(items[i])
#     LDA $i
#       INC 1
#       STO $i
#     BAA -outputNextNumber
    continue

# -halt
# HLT