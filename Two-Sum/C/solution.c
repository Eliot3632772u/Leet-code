#include <stdlib.h>
#include <stdio.h>

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

void insertToHashMap(map **hashMap, int hashKey, int key, int value) {

    map *new = newMap(key, value);

    map *head = hashMap[hashKey];

    if (head != NULL) {

        pushFront(&head, new);
        return;
    }

    hashMap[hashKey] = new;
}

int hash(int key, int mapSize) {

    if (key < 0)
        key = -key;

    return key % mapSize;
}

map* getValue(map **hashMap, int key, int number) {

    if (hashMap[key] == NULL) return NULL;

    map *head = hashMap[key];

    while (head){

        if (head->key == number) return head;
        head = head->next;
    }

    return NULL;
}

int* twoSum(int* nums, int numsSize, int target, int* returnSize) {
    
    int mapSize = 10000;
    map **hashMap = newHashMap(mapSize);
    int *res = malloc(sizeof(int) * 2);

    int i = 0;
    while (i < numsSize) {

        int key = hash(nums[i], mapSize);
        map *complement = getValue(hashMap, key, nums[i]);
        if (complement != NULL) {
            res[0] = i;
            res[1] = complement->val;

            return res;
        }

        insertToHashMap(hashMap, key, nums[i], i);

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