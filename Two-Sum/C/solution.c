#include <stdio.h>
#include <stdlib.h>

typedef struct map {
    int key;
    int val;
    struct map* next;
}map;

map* newMap(int key, int value) {

    map* res = malloc(sizeof(map));
    res->key = key;
    res->val = value;
    res->next = NULL;

    return res;
}

map** newHashMap(int size) {

    map** res = calloc(size, sizeof(map*));
    return res;
}

void pushFront(map **old, map* new) {

    new->next = *old;
    *old = new;
}

int* twoSum(int* nums, int numsSize, int target, int* returnSize) {
    
    map **hashMap = newHashMap(2000000001);
    int roundNum = 1000000000;
    int *res = malloc(sizeof(int) * 2);

    int i = 0;
    while (i < numsSize) {

        map *complement = hashMap[(target - nums[i]) + roundNum];
        if (complement != NULL){
            res[0] = i;
            res[1] = complement->val;

            return res;
        }
        else {
            map *new = newMap(nums[i] + roundNum, i);
            hashMap[nums[i] + roundNum] = new;
        }

        i++;
    }
    
    return nums;
}

int main() {
    int nums[] = {2, 7, 11, 15};
    int target = 9;
    int returnSize;
    int* result = twoSum(nums, sizeof(nums) / sizeof(nums[0]), target, &returnSize);
    
    if (result != NULL) {
        printf("Indices: [%d, %d]\n", result[0], result[1]);
        free(result); // Remember to free the allocated memory
    } else {
        printf("No two sum solution found.\n");
    }
    
    return 0;
}