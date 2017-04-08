package main

import (
	"log"
	"os"
	"path/filepath"
	"strings"
)

// File - Contains information about a file
type File struct {
	Path      string
	RelPath   string
	Name      string
	Extension string
	File      os.FileInfo
}

// Scan - Walk a directory and add all files to a map
func Scan(dir string, filters []string) map[string]File {
	log.Printf("%+v", filters)
	fileMap := make(map[string]File)

	filepath.Walk(dir, func(path string, file os.FileInfo, err error) error {
		if !Contains(path, filters) {
			if !file.IsDir() {
				extension := ""
				dotPos := strings.LastIndex(file.Name(), ".")

				if dotPos != -1 {
					extension = string(file.Name()[dotPos:])
				}

				newFile := File{}
				newFile.Path = path
				newFile.RelPath = strings.Replace(path, dir, "", -1)
				newFile.Name = file.Name()
				newFile.Extension = extension
				newFile.File = file

				fileMap[newFile.RelPath] = newFile
			}
		}

		return nil
	})

	return fileMap
}

// Diff - Get a list of files that exist in source and not in destination
func Diff(source, destination map[string]File) []File {
	var files []File

	for name, file := range source {
		if _, ok := destination[name]; !ok {
			files = append(files, file)
		}
	}

	return files
}

// Contains - Check whether a given string is in an array of strings
func Contains(key string, values []string) bool {
	for _, value := range values {
		if key == value || strings.Contains(value, key) || strings.Contains(key, value) {
			return true
		}
	}

	return false
}
