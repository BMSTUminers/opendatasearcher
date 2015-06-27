#coding:utf-8

require "pry"
require "json"
require_relative "./sentence_parser.rb"
require_relative "./cluster.rb"

def flatten()
	data = JSON.load(File.read("clust.json"))

	data.map! do |x|
		x.map do |y|
			y.map do |z|
				z.map do |w|
					w.flatten
				end
			end
		end
	end

	File.write("clust_flatten.json" , data.to_json)
end


def iterate(data, &closure)
	if data[0].class == Array then
		data.each {|x| iterate(x, &closure)}
	else
		yield data 
	end

end

if __FILE__ == $0
	
	data = JSON.load(File.read("clust_flatten.json"))

	parser = Parser.new
	sw = StopWords.new

	keywords = []
	n = 0
	iterate(data) do |titles|
		text = titles.join(" ")

		t_data = parser.tokenize(text ,:default)
		tokens = t_data.select{|x| x[1] == :word }.map{|x| x[0]}.map{|x|  parser.convert(x)[0] }.select{|x| not sw.check?(x)}

		friq = tokens.reduce(Hash.new(0)) { |a, b| a[b] += 1; a }

		#friq.sort_by{|a, b| -b}[0..10]

		keywords.push (friq.sort_by{|a, b| -b}[0..10])

		n+=1;
		
		p n

	end 

	File.write("keywords.json" , keywords.to_json)

end