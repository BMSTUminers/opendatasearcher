#coding:utf-8
require "pry"
require "json"
require "levenshtein"

geo_regexp=/^\d\d[\,\.]\d+$/

x_words = ["X", "Широта" , "latitude" , "Координата X"]
y_words = ["Y", "Долгота", "longtitude", "Координата Y" ]

if __FILE__ == $0
	ARGV.each do |file|
		geo = {}
		data = nil
		begin
			data = JSON.load(File.open(file))
		rescue
			next
		end
		next if data == nil
		next if data.size == 0
		data[0].each do |key,val|
			if val != nil
				if val.match(geo_regexp) != nil
					geo[key] = val
					p key
				end
			end
		end
		if geo.size == 2 then
			res = {}
			geo.keys.each do |key|
				x_dist = x_words.inject(100) { |mem, var|  mem = [Levenshtein.distance(var, key), mem].min }
				y_dist = y_words.inject(100) { |mem, var|  mem = [Levenshtein.distance(var, key), mem].min }
				p x_dist
				p y_dist
				if x_dist < y_dist then
					res["X"] = key
				end
				if x_dist > y_dist then
					res["Y"] = key
				end

				if x_dist == y_dist
					if res.size == 0
						res["X"] = key
					else
						res["Y"] = key
					end
				end
			end
			geo = data.map{|x| {"X" => x[res["X"]] , "Y" => x[res["Y"]] } }
			fname = File.basename(file,".*")
			File.write(fname + ".geo.json", geo.to_json)
		end
	end
end